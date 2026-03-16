package com.gestorsolar.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "PROYECTO")
@SQLDelete(sql = "UPDATE PROYECTO SET deleted = true WHERE id=?")
// En Hibernate 6 @Where es deprecada, la alternativa limpia dependiente de vendor es @SQLRestriction. Usando Where para compatibilidad o filtrado directo.
// La solicitud es con Spring boot 4 (inexistente)/3.4, Hibernate >6.2: @SQLRestriction("deleted = false")
@org.hibernate.annotations.SQLRestriction("deleted = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cedula_nit_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proyecto", nullable = false)
    private TipoProyecto tipoProyecto;

    private Integer estrato;

    @Column(name = "numero_usuario_energia")
    private String numeroUsuarioEnergia;

    @Column(name = "numero_transformador")
    private String numeroTransformador;

    private boolean deleted = false;

    // Relaciones 1 a 1 simuladas, Fetch=LAZY (o mapped en bidireccional si se requiere)
    @OneToOne(mappedBy = "proyecto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tecnicos tecnicos;

    @OneToOne(mappedBy = "proyecto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cronograma cronograma;
}
