package tn.universite.etudiant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import java.util.List;
@RestController @RequestMapping("/api/etudiants")
public class EtudiantController {
 private final EtudiantRepository repo;
 public EtudiantController(EtudiantRepository repo){this.repo=repo;}
 @GetMapping public List<Etudiant> all(){return repo.findAll();}
 @GetMapping("/{id}") public Etudiant one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Etudiant create(@Valid @RequestBody Etudiant e){e.setNom(e.getNom().trim());return repo.save(e);}
 @PutMapping("/{id}") public Etudiant update(@PathVariable Long id,@Valid @RequestBody Etudiant e){
  Etudiant old=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
  old.setNom(e.getNom());old.setPrenom(e.getPrenom());old.setEmail(e.getEmail());return repo.save(old);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){if(!repo.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND);repo.deleteById(id);}
}
