package com.key.win.item.controller;

import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.web.Result;
import com.key.win.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
//import javax.validation.constraints.NotNull;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/deductInventory")
    public Result deductInventory(/*@NotNull*/ String productId, HttpServletRequest request) throws ServiceException {
        return  itemService.deductInventory(productId);
    }

}
