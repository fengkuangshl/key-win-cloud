package com.key.win.refresh.ctrl;

import com.key.win.refresh.vo.RefreshScopeTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refreshScopeTemplateTemplateCtrl/*")
public class RefreshScopeTemplateTemplateCtrl {

    @Autowired
    private RefreshScopeTemplateVo refreshScopeTemplateVo;


    @GetMapping("/getRefreshScopeValue")
    public String getJpaTemplateByPaged() {
        return refreshScopeTemplateVo.toString();
    }



}
