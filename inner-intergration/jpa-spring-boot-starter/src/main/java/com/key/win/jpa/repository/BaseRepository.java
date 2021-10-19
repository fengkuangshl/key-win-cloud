package com.key.win.jpa.repository;

import com.key.win.jpa.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseModel,ID> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T> {

    @Transactional
    default void logicDeleted(ID id){
        T t =   this.getOne(id);
        t.setEnableFlag(false);
        this.saveAndFlush(t);
    };

    @Transactional
    default  void logicDeleted(T t){
        logicDeleted((ID) t.getId());
    }

    @Transactional
    default void logicDeleted(Iterable<? extends T> entities) {
        entities.forEach(entitiy -> logicDeleted((ID) entitiy.getId()));
    }

    @Transactional
    default void logicDeletedByIds(Iterable<? extends ID> ids) {
        ids.forEach(id -> logicDeleted(id));
    }
}
