
// File: src/main/java/org/example/lesspass/model/Pass.java
        package org.example.lesspass.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contrasenas")
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpass")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser", nullable = false)
    private User user;

    @Column(name = "Usuario")
    private String usuario;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "url")
    private String url;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public String toString() {
        return "Pass{id=" + id + ", userId=" + (user != null ? user.getId() : null) +
                ", usuario='" + usuario + '\'' + ", url='" + url + '\'' + '}';
    }
}
