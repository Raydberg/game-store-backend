package com.gamestore.address.controllers;

import com.gamestore.address.services.AddressService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class AddressController {
    private final AddressService addressService;

    @GetMapping("")
    public ResponseEntity<?> getAllCartShop(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página debe ser 0 o mayor") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño debe ser al menos 1") int size
    ) {
        return ResponseEntity.ok("");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok("");
    }


    @PostMapping("")
    public ResponseEntity<?> create() {
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        return ResponseEntity.ok("");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }


}
