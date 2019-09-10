package com.company.ReneSerulleU1Capstone.controller;

import com.company.ReneSerulleU1Capstone.servicelayer.FeeType;
import com.company.ReneSerulleU1Capstone.servicelayer.InventoryServiceLayer;
import com.company.ReneSerulleU1Capstone.viewmodel.*;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RestController for paths related to the management of store inventory and settings (such as updating tax rates)
 *
 * see /test/resources/path-samples.txt for sample paths and data for testing on Postman
 */
@RestController
@Validated
public class ItemInventoryController {

    @Autowired
    private InventoryServiceLayer sl;

    @RequestMapping(value = "/item/inventory", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<ItemViewModel> addItems(@RequestBody @Valid List<ItemViewModel> items)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        return sl.add(items);
    }

    @RequestMapping(value = "/item/inventory", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateItems(@RequestBody @Valid List<ItemViewModel> items)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        sl.update(items);
    }

    @RequestMapping(value = "/item/{type}/inventory/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String type, @PathVariable Long id) throws InvalidTypeIdException {
        sl.delete(type, id);
    }

    @RequestMapping(value = "/purchase/invoice", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<?> findAllInvoices()
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        return sl.findAll(PurchaseViewModel.class);
    }

    @RequestMapping(value = "/inventory/fee", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.MULTI_STATUS)
    public List<?> addFees(@RequestBody ArrayList<PurchaseFeeViewModel> purchaseFees) throws InvalidClassException {
        return sl.add(purchaseFees);
    }

    @RequestMapping(value = "/inventory/fee", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateFeesMap(@RequestBody Map<String, PurchaseFeeViewModel> purchaseFeeMap)
            throws InvalidClassException {
        sl.update(purchaseFeeMap);
    }

    @RequestMapping(value = "/inventory/{feeType}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<?> findAllFeeType(@PathVariable @Pattern(regexp = FeeType.pathRegex) String feeType)
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        return sl.findAllFeeType(feeType);
    }

    @RequestMapping(value = "/inventory/{feeType}/{value}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Object findFeeTypeOfValue(@PathVariable String feeType, @PathVariable String value)
            throws InvalidClassException, InstantiationException, IllegalAccessException {
        return sl.findFeeType(feeType, value);
    }

    @RequestMapping(value = "/inventory/{feeType}/{value}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteFeeTypeOfValue(@PathVariable String feeType, @PathVariable String value)
            throws InvalidClassException {
        sl.deleteFeeType(feeType, value);
    }

    @RequestMapping(value = "/inventory/{feeType}/reset", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void resetFeeType(@PathVariable String feeType) {
        sl.resetFeeType(feeType);
    }
}
