package senai.fatesg.com.cgponto.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Justificativa {

    private Integer idJustificativa;

    private String titulo;
    private String descricao;
    private byte[] anexoDocumento;
    private String horasDiariaInicio;
    private String horasDiariaTermino;
    private String dataInicio;
    private String dataTermino;
    private String status;
    private Date data;
    private Long idUser;
}
