package com.key.win.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.key.win.common.web.PageResult;
import com.key.win.client.dao.GatewayRoutesDao;
import com.key.win.client.dto.GatewayFilterDefinition;
import com.key.win.client.dto.GatewayPredicateDefinition;
import com.key.win.client.dto.GatewayRouteDefinition;
import com.key.win.client.entity.GatewayRoutes;
import com.key.win.client.service.DynamicRouteService;
import com.key.win.client.vo.GatewayRoutesVO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

import static com.key.win.client.routes.RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX;

@Service
@SuppressWarnings("all")
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware, DynamicRouteService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private GatewayRoutesDao gatewayRoutesDao;

    private ApplicationEventPublisher publisher;

    /**
     * ????????? ????????????
     */
    private static MapperFacade routeDefinitionMapper;
    private static MapperFacade routeVOMapper;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(GatewayRouteDefinition.class, GatewayRoutes.class)
                .exclude("filters")
                .exclude("predicates")
                .byDefault();
        routeDefinitionMapper = mapperFactory.getMapperFacade();
        //  routeVOMapper
        mapperFactory.classMap(GatewayRoutes.class, GatewayRoutesVO.class).byDefault();
        routeVOMapper = mapperFactory.getMapperFacade();

    }


    /**
     * ???spring????????????
     * ????????????
     */
    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * Set the ApplicationEventPublisher that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param applicationEventPublisher event publisher to be used by this object
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * ????????????
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String add(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes gatewayRoutes = transformToGatewayRoutes(gatewayRouteDefinition);
        gatewayRoutes.setDelFlag(0);
        gatewayRoutes.setCreateTime(new Date());
        gatewayRoutes.setUpdateTime(new Date());
        gatewayRoutesDao.insertSelective(gatewayRoutes);
        gatewayRouteDefinition.setId(gatewayRoutes.getId());
        stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(), JSONObject.toJSONString(gatewayRouteDefinition));
        return gatewayRoutes.getId();
    }

    /**
     * ????????????
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String update(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes gatewayRoutes = transformToGatewayRoutes(gatewayRouteDefinition);
        gatewayRoutes.setCreateTime(new Date());
        gatewayRoutes.setUpdateTime(new Date());
        gatewayRoutesDao.updateByPrimaryKeySelective(gatewayRoutes);
        stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete(gatewayRouteDefinition.getId());
        stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(), JSONObject.toJSONString(gatewayRouteDefinition));
        return gatewayRouteDefinition.getId();
    }


    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
        gatewayRoutesDao.deleteByPrimaryKey(id);
        stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete(id);

        return "success";
//        try {
//            this.routeDefinitionWriter.delete(Mono.just(id));
//            notifyChanged();
//            return "delete success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "delete fail";
//        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public PageResult<GatewayRoutesVO> findAll(Map<String, Object> params) {
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        List<GatewayRoutes> alls = gatewayRoutesDao.findAll(new HashMap());
        PageInfo<GatewayRoutesVO> pageInfo = new PageInfo<>(routeVOMapper.mapAsList(alls, GatewayRoutesVO.class));
        return PageResult.<GatewayRoutesVO>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    /**
     * @return
     */
    @Override
    public String synchronization() {
        HashMap map = new HashMap();
        map.put("delFlag", 0);
        List<GatewayRoutes> gatewayRoutes = gatewayRoutesDao.findAll(map);
        for (GatewayRoutes route : gatewayRoutes) {
            GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder()
                    .description(route.getDescription())
                    .id(route.getId())
                    .order(route.getOrder())
                    .uri(route.getUri())
                    .build();
            List<GatewayFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(route.getFilters(), GatewayFilterDefinition.class);
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(route.getPredicates(), GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);
            stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(route.getId(), JSONObject.toJSONString(gatewayRouteDefinition));


        }

        return "success";
    }

    /**
     * ??????????????????
     *
     * @param params
     * @return
     */
    @Override
    public String updateFlag(Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        Integer flag = MapUtils.getInteger(params, "flag");
        GatewayRoutes gatewayRoutes = gatewayRoutesDao.selectByPrimaryKey(id);
        if (gatewayRoutes == null) {
            return "route is empty";
        }

        if (flag == 1) {
            stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete(id);
        } else {
            GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder()
                    .description(gatewayRoutes.getDescription())
                    .id(gatewayRoutes.getId())
                    .order(gatewayRoutes.getOrder())
                    .uri(gatewayRoutes.getUri())
                    .build();
            List<GatewayFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(gatewayRoutes.getFilters(), GatewayFilterDefinition.class);
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(gatewayRoutes.getPredicates(), GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);
            stringRedisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRoutes.getId(), JSONObject.toJSONString(gatewayRouteDefinition));
        }

        gatewayRoutes.setDelFlag(flag);
        gatewayRoutes.setUpdateTime(new Date());
        int success = gatewayRoutesDao.updateByPrimaryKeySelective(gatewayRoutes);
        return success > 0 ? "????????????" : "????????????";
    }

    /**
     * ??????????????????  GatewayRoutes
     *
     * @param gatewayRouteDefinition
     * @return
     */
    private GatewayRoutes transformToGatewayRoutes(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes definition = new GatewayRoutes();
        routeDefinitionMapper.map(gatewayRouteDefinition, definition);
        //????????????id
        if (!StringUtils.isNotBlank(definition.getId())) {
            definition.setId(java.util.UUID.randomUUID().toString().toUpperCase().replace("-", ""));
        }
        String filters = JSONArray.toJSONString(gatewayRouteDefinition.getFilters());
        String predicates = JSONArray.toJSONString(gatewayRouteDefinition.getPredicates());
        definition.setFilters(filters);
        definition.setPredicates(predicates);

        return definition;
    }

    /**
     * ???????????? ?????? ????????????
     */
//    @PostConstruct
    public void main() {
        RouteDefinition definition = new RouteDefinition();
        definition.setId("jd");
        URI uri = UriComponentsBuilder.fromUriString("lb://user-center").build().toUri();
//         URI uri = UriComponentsBuilder.fromHttpUrl("http://baidu.com").build().toUri();
        definition.setUri(uri);
        definition.setOrder(11111);

        //?????????????????????
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");

        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("pattern", "/jd/**");
        predicate.setArgs(predicateParams);
        //??????Filter
        FilterDefinition filter = new FilterDefinition();
        filter.setName("StripPrefix");
        Map<String, String> filterParams = new HashMap<>(8);
        //???_genkey_????????????????????????org.springframework.cloud.gateway.support.NameUtils???
        filterParams.put("_genkey_0", "1");
        filter.setArgs(filterParams);

        definition.setFilters(Arrays.asList(filter));
        definition.setPredicates(Arrays.asList(predicate));

        System.out.println("definition:" + JSON.toJSONString(definition));
        stringRedisTemplate.opsForHash().put(GATEWAY_ROUTES_PREFIX, "key", JSON.toJSONString(definition));
    }
}
