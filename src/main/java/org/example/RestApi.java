package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@Repository
interface ItemRepository extends CrudRepository<Item, Long>{}

@RestController
@RequestMapping(path = "/api/items")
public class RestApi {

    @Autowired
    private ItemRepository repository;


    @GetMapping
    public Iterable<Item> getAll(){
        return repository.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") Long id){

        Item item = repository.findById(id).orElse(null);

        if(item == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Item item){
        item.setId(null);
        repository.save(item);
        return ResponseEntity.created(URI.create("/api/"+item.getId())).build();
    }
}
