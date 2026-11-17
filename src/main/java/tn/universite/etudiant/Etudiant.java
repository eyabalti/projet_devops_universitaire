package tn.universite.etudiant;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name="etudiant")
public class Etudiant {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String nom;
 @NotBlank private String prenom;
 @Email @NotBlank @Column(unique=true) private String email;
 public Etudiant(){}
 public Etudiant(String nom,String prenom,String email){this.nom=nom;this.prenom=prenom;this.email=email;}
 public Long getId(){return id;} public String getNom(){return nom;} public void setNom(String v){nom=v;}
 public String getPrenom(){return prenom;} public void setPrenom(String v){prenom=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
}
