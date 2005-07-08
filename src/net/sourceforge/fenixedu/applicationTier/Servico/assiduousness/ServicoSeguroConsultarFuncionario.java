package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.Horario;
import net.sourceforge.fenixedu.domain.HorarioTipo;
import net.sourceforge.fenixedu.domain.NonTeacherEmployee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StatusAssiduidade;
import net.sourceforge.fenixedu.persistenceTierJDBC.ICentroCustoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IHorarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IHorarioTipoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IRegimePersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IStatusAssiduidadePersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quit�rio & Tania Pous�o
 */
public class ServicoSeguroConsultarFuncionario extends ServicoSeguro {

    private int _numMecanografico;

    private Funcionario _funcionario = null;

    private StatusAssiduidade _status = null;

    private CostCenter _centroCusto = null;

    private Person _pessoa = null;

    private NonTeacherEmployee _funcNaoDocente = null;

    private List _rotacaoHorario = null;

    private HashMap _listaRegimesRotacao = null;

    public ServicoSeguroConsultarFuncionario(ServicoAutorizacao servicoAutorizacaoLerFuncionario,
            int numMecanografico) {
        super(servicoAutorizacaoLerFuncionario);
        _numMecanografico = numMecanografico;
    }

    public void execute() throws NotExecuteException {

        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if ((_funcionario = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico,
                Calendar.getInstance().getTime())) == null) {
            throw new NotExecuteException("error.funcionario.naoExiste");
        }

        IStatusAssiduidadePersistente iStatusPersistente = SuportePersistente.getInstance()
                .iStatusAssiduidadePersistente();
        if (_funcionario.getChaveStatus() != null) {
            if ((_status = iStatusPersistente.lerStatus(_funcionario.getChaveStatus().intValue())) == null) {
                throw new NotExecuteException("error.assiduidade.naoExiste");
            }
        } else {
            throw new NotExecuteException("error.assiduidade.situacao.nao.regularizada");
        }

        ICentroCustoPersistente iCentroCustoPersistente = SuportePersistente.getInstance()
                .iCentroCustoPersistente();
        if (_funcionario.getChaveCCCorrespondencia() != null) {
            if ((_centroCusto = iCentroCustoPersistente.lerCentroCusto(_funcionario
                    .getChaveCCCorrespondencia().intValue())) == null) {
                throw new NotExecuteException("error.centroCusto.naoExiste");
            }
        } else {
            throw new NotExecuteException("error.assiduidade.situacao.nao.regularizada");
        }

        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if ((_pessoa = iPessoaPersistente.lerPessoa(_funcionario.getChavePessoa())) == null) {
            throw new NotExecuteException("error.pessoa.naoExiste");
        }

        //		IFuncNaoDocentePersistente iFuncNaoDocentePersistente =
        //			SuportePersistente.getInstance().iFuncNaoDocentePersistente();
        //		if ((_funcNaoDocente =
        //			iFuncNaoDocentePersistente.lerFuncNaoDocentePorFuncionario(_funcionario.getCodigoInterno()))
        //			== null) {
        //			throw new NotExecuteException("error.funcionario.naoExiste");
        //		}

        IHorarioPersistente iHorarioPersistente = SuportePersistente.getInstance().iHorarioPersistente();
        // Leitura de uma hor�rio seja ele rotativo ou n�o
        _rotacaoHorario = iHorarioPersistente.lerHorarioActualPorNumMecanografico(_numMecanografico);
        //_rotacaoHorario =
        // iHorarioPersistente.lerRotacoesPorNumMecanografico(_numMecanografico);
        if (_rotacaoHorario == null) {
            throw new NotExecuteException("error.funcionario.semHorario");
        } else if (_rotacaoHorario.isEmpty()) {
            throw new NotExecuteException("error.funcionario.semAssiduidade");
        }

        IHorarioTipoPersistente iHorarioTipoPersistente = SuportePersistente.getInstance()
                .iHorarioTipoPersistente();
        IRegimePersistente iRegimePersistente = SuportePersistente.getInstance().iRegimePersistente();

        _listaRegimesRotacao = new HashMap();
        ListIterator iterador = _rotacaoHorario.listIterator();

        Horario horario = null;
        HorarioTipo horarioTipo = null;
        List listaIdRegimes = null;
        List listaRegimes = null;
        while (iterador.hasNext()) {
            horario = (Horario) iterador.next();

            if (horario.getChaveHorarioTipo() == 0) {
                if ((listaIdRegimes = iHorarioPersistente.lerRegimes(horario.getCodigoInterno())) == null) {
                    throw new NotExecuteException("error.regime.impossivel");
                }
                if ((listaRegimes = iRegimePersistente.lerDesignacaoRegimes(listaIdRegimes)) == null) {
                    throw new NotExecuteException("error.regime.impossivel");
                }
            } else {
                if ((horarioTipo = iHorarioTipoPersistente.lerHorarioTipo(horario.getChaveHorarioTipo())) == null) {
                    throw new NotExecuteException("error.funcionario.semHorario");
                }

                /* regista a modalidade e a sigla */
                horario.transforma(horarioTipo);

                if ((listaIdRegimes = iHorarioTipoPersistente.lerRegimes(horario.getChaveHorarioTipo())) == null) {
                    throw new NotExecuteException("error.regime.impossivel");
                }
                if ((listaRegimes = iRegimePersistente.lerDesignacaoRegimes(listaIdRegimes)) == null) {
                    throw new NotExecuteException("error.regime.impossivel");
                }
            }

            _listaRegimesRotacao.put(new Integer(horario.getPosicao()), listaRegimes);
        }
    }

    public Funcionario getFuncionario() {
        return _funcionario;
    }

    public StatusAssiduidade getStatusAssiduidade() {
        return _status;
    }

    public CostCenter getCentroCusto() {
        return _centroCusto;
    }

    public Person getPessoa() {
        return _pessoa;
    }

    public NonTeacherEmployee getFuncNaoDocente() {
        return _funcNaoDocente;
    }

    public List getRotacaoHorario() {
        return _rotacaoHorario;
    }

    public HashMap getListaRegimesRotacao() {
        return _listaRegimesRotacao;
    }
}