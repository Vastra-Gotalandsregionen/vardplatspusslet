package se.vgregion.vardplatspusslet.domain.jpa;




import javax.persistence.*;

@Entity
@Table(name= "careBurdenCategori")
public class CareBurdenKategori {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String kategori;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
