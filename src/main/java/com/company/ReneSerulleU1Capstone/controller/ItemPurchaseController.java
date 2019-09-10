package com.company.ReneSerulleU1Capstone.controller;

import com.company.ReneSerulleU1Capstone.servicelayer.ServiceLayer;
import com.company.ReneSerulleU1Capstone.viewmodel.ItemViewModel;
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

/**
 * RestController for paths related to the purchase of an item
 *
 * see /test/resources/path-samples.txt for sample paths and data for testing on Postman
 */
@RestController
@Validated
public class ItemPurchaseController {

    @Autowired
    private ServiceLayer sl;

    @RequestMapping(value = "/itemType", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> getAllItemTypes()
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        return sl.findAllProductTypes();
    }

    @RequestMapping(value = "/salesTaxRate", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, BigDecimal> getAllSalesTaxRate()
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        return sl.findAllAsMap(SalesTaxRateViewModel.class);
    }

    @RequestMapping(value = "/processingFee", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, BigDecimal> getAllProcessingFee()
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        return sl.findAllAsMap(ProcessingFeeViewModel.class);
    }

    @RequestMapping(value = "/item/{type}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<ItemViewModel> getAllItemsOfType(@PathVariable String type)
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        return sl.findAll(type);
    }

    @RequestMapping(value = "/item/{type}/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public ItemViewModel getAllItemsOfTypeAndId(@PathVariable String type, @PathVariable Long id)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        return sl.findItem(type, id);
    }

    @RequestMapping(value = "/item/{type}/attributes", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> getAllItemTypeAttributes(@PathVariable String type) throws InvalidTypeIdException {
        return sl.findSearchableAttributes(type);
    }

    @RequestMapping(value = "/item/{type}/{attribute}/{value}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<ItemViewModel> getAllItemsWithAttributeOfValue(
            @PathVariable String type, @PathVariable String attribute, @PathVariable String value )
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        return sl.findBy(type, attribute, value);
    }

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public PurchaseViewModel addPurchase(@RequestBody @Valid PurchaseViewModel purchaseViewModel)
            throws InvalidTypeIdException, InvalidAttributeValueException, ServiceUnavailableException,
            InstantiationException, IllegalAccessException {
        return sl.add(purchaseViewModel);
    }

    @RequestMapping(value = "/purchase/invoice/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public PurchaseViewModel getPurchase(@PathVariable Long id)
            throws InvalidTypeIdException, InvalidClassException, IllegalAccessException, InstantiationException {
        return sl.find(PurchaseViewModel.class, id);
    }

}
