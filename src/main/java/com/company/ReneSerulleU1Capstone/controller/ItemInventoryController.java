package com.company.ReneSerulleU1Capstone.controller;

import com.company.ReneSerulleU1Capstone.model.Item;
import com.company.ReneSerulleU1Capstone.servicelayer.InventoryServiceLayer;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InvalidClassException;
import java.util.List;

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
}
