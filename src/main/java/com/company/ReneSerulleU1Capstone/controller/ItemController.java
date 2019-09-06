package com.company.ReneSerulleU1Capstone.controller;

import com.company.ReneSerulleU1Capstone.dao.ItemDao;
import com.company.ReneSerulleU1Capstone.model.Game;
import com.company.ReneSerulleU1Capstone.model.Item;
import com.company.ReneSerulleU1Capstone.servicelayer.InventoryServiceLayer;
import com.company.ReneSerulleU1Capstone.servicelayer.ServiceLayer;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.InvalidClassException;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class ItemController {

    @Autowired
    ItemDao<Game> gameItemDao;

    @Autowired
    ServiceLayer sl;

    @Autowired
    InventoryServiceLayer isl;

    @RequestMapping(value = "/item/inventory", method = RequestMethod.POST)
    public List<Item> addItem(@RequestBody @Valid List<Item> items) {

        return items;
    }

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    public PurchaseViewModel addPurchase(@RequestBody @Valid PurchaseViewModel purchaseViewModel) {

        return purchaseViewModel;
    }

    @RequestMapping(value = "/itemType", method = RequestMethod.GET)
    public List<String> getAllItemTypes() throws InvalidClassException {
        return sl.findAllProductTypes();
    }

    @RequestMapping(value = "/salesTaxRate", method = RequestMethod.GET)
    public List<?> getAllSalesTaxRate() throws InvalidClassException {
        return sl.findAll(SalesTaxRateViewModel.class);
    }
}
