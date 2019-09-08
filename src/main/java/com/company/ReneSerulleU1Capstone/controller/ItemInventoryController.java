package com.company.ReneSerulleU1Capstone.controller;

import com.company.ReneSerulleU1Capstone.model.Item;
import com.company.ReneSerulleU1Capstone.servicelayer.InventoryServiceLayer;
import com.company.ReneSerulleU1Capstone.viewmodel.ProcessingFeeViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseFee;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;
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

@RestController
@Validated
public class ItemInventoryController {

    @Autowired
    private InventoryServiceLayer sl;

    @RequestMapping(value = "/item/inventory", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<Item> addItems(@RequestBody @Valid List<Item> items) throws InvalidTypeIdException {
        return sl.add(items);
    }

    @RequestMapping(value = "/item/inventory", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateItems(@RequestBody @Valid List<Item> items) throws InvalidTypeIdException {
        sl.update(items);
    }

    @RequestMapping(value = "/item/{type}/inventory/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String type, @PathVariable Long id) throws InvalidTypeIdException {
        sl.delete(type, id);
    }

    @RequestMapping(value = "/purchase/invoice", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<?> findAllInvoices() throws InvalidClassException {
        return sl.findAll(PurchaseViewModel.class);
    }

    @RequestMapping(value = "/inventory/fee", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<?> addFees(@RequestBody ArrayList<PurchaseFee> purchaseFees) throws InvalidClassException {
        return sl.add(purchaseFees);
    }

    @RequestMapping(value = "/inventory/fee", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateFeesMap(@RequestBody Map<String, PurchaseFee> purchaseFeeMap)
            throws InvalidClassException {
        sl.update(purchaseFeeMap);
    }

    @RequestMapping(value = "/inventory/{feeType}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<?> findAllFeeType(@PathVariable @Pattern(regexp = "^processingFee|salesTaxRate$") String feeType)
            throws InvalidClassException {
        switch (feeType) {
            case "processingFee":
                return sl.findAll(ProcessingFeeViewModel.class);
            case "salesTaxRate":
                return sl.findAll(SalesTaxRateViewModel.class);
            default:
                throw new IllegalArgumentException("Invalid feeType. Must be processingFee or salesTaxRate");
        }
    }

    @RequestMapping(value = "/inventory/{feeType}/{value}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Object findFeeTypeOfValue(@PathVariable @Pattern(regexp = "^processingFee|salesTaxRate$") String feeType,
                                      @PathVariable String value)
            throws InvalidClassException {
        switch (feeType) {
            case "processingFee":
                return sl.find(ProcessingFeeViewModel.class, value);
            case "salesTaxRate":
                return sl.find(SalesTaxRateViewModel.class, value);
            default:
                throw new IllegalArgumentException("Invalid feeType. Must be processingFee or salesTaxRate");
        }
    }

    @RequestMapping(value = "/inventory/{feeType}/{value}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteFeeTypeOfValue(@PathVariable @Pattern(regexp = "^processingFee|salesTaxRate$") String feeType,
                                     @PathVariable String value)
            throws InvalidClassException {
        switch (feeType) {
            case "processingFee":
                sl.delete(ProcessingFeeViewModel.class, value);
                break;
            case "salesTaxRate":
                sl.delete(SalesTaxRateViewModel.class, value);
                break;
            default:
                throw new IllegalArgumentException("Invalid feeType. Must be processingFee or salesTaxRate");
        }
    }
}
