package com.example.pokemonapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

    @RestController
    @RequestMapping("/api/pokemons")
    public class PokemonController {

        private final List<Pokemons> pokemons = new ArrayList<>();
        private final AtomicLong contador = new AtomicLong(0);

        public PokemonController() {
            pokemons.add(new Pokemons(
                    contador.incrementAndGet(), "Pikachu", "Elétrico"));
            pokemons.add(new Pokemons(
                    contador.incrementAndGet(), "Charmander", "Fogo"));
            pokemons.add(new Pokemons(
                    contador.incrementAndGet(), "Bulbasaur", "Planta"));
        }

        @GetMapping
        public List<Pokemons> listarTodos() {
            return pokemons.stream().limit(50).toList();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Pokemons> buscarPorId(
                @PathVariable Long id) {
            return pokemons.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Pokemons> cadastrar(
                @RequestBody Pokemons pokemon) {
            pokemon.setId(contador.incrementAndGet());
            pokemons.add(pokemon);
            return ResponseEntity.status(HttpStatus.CREATED).body(pokemon);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Pokemons> atualizar(
                @PathVariable Long id,
                @RequestBody Pokemons dados) {
            for (Pokemons pokemon : pokemons) {
                if (pokemon.getId().equals(id)) {
                    pokemon.setNome(dados.getNome());
                    pokemon.setTipo(dados.getTipo());
                    return ResponseEntity.ok(pokemon);
                }
            }
            return ResponseEntity.notFound().build();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
            boolean removido = pokemons.removeIf(
                    pokemon -> pokemon.getId().equals(id));

            if (!removido) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.noContent().build();
        }
    }
