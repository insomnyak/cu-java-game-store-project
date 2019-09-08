package com.company.ReneSerulleU1Capstone.controller;

import com.company.ReneSerulleU1Capstone.model.Item;
import com.company.ReneSerulleU1Capstone.servicelayer.ServiceLayer;
import com.company.ReneSerulleU1Capstone.viewmodel.ProcessingFeeViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.naming.ServiceUnavailableException;
import javax.validation.Valid;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class ItemPurchaseController {

    @Autowired
    private ServiceLayer sl;

    @RequestMapping(value = "/itemType", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> getAllItemTypes() throws InvalidClassException {
        return sl.findAllProductTypes();
    }

    @RequestMapping(value = "/salesTaxRate", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, BigDecimal> getAllSalesTaxRate() throws InvalidClassException {
        return sl.findAllAsMap(SalesTaxRateViewModel.class);
    }

    @RequestMapping(value = "/processingFee", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, BigDecimal> getAllProcessingFee() throws InvalidClassException {
        return sl.findAllAsMap(ProcessingFeeViewModel.class);
    }

    @RequestMapping(value = "/item/{type}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<? extends Item> getAllItemsOfType(@PathVariable String type) throws InvalidTypeIdException {
        return sl.findAll(type);
    }

    @RequestMapping(value = "/item/{type}/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Item getAllItemsOfTypeAndId(@PathVariable String type, @PathVariable Long id)
            throws InvalidTypeIdException {
        return sl.find(type, id);
    }

    @RequestMapping(value = "/item/{type}/attributes", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> getAllItemTypeAttributes(@PathVariable String type) throws InvalidTypeIdException {
        return sl.findSearchableAttributes(type);
    }

    @RequestMapping(value = "/item/{type}/{attribute}/{value}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<? extends Item> getAllItemsWithAttributeOfValue(
            @PathVariable String type, @PathVariable String attribute, @PathVariable String value )
            throws InvalidTypeIdException {
        return sl.findBy(type, attribute, value);
    }

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public PurchaseViewModel addPurchase(@RequestBody @Valid PurchaseViewModel purchaseViewModel)
            throws InvalidTypeIdException, InvalidAttributeValueException, ServiceUnavailableException {
        return sl.add(purchaseViewModel);
    }

    @RequestMapping(value = "/purchase/invoice/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public PurchaseViewModel getPurchase(@PathVariable Long id) throws InvalidTypeIdException, InvalidClassException {
        return (PurchaseViewModel) sl.find(PurchaseViewModel.class, id);
    }

}
