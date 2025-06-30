package br.ufscar.dc.dsw.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.domain.Empresa;

@Component
public class UniqueCNPJEmpresaValidator implements ConstraintValidator<UniqueCNPJEmpresa, String> {

	@Autowired
	private IEmpresaDAO dao;

	@Override
	public boolean isValid(String cnpj, ConstraintValidatorContext context) {
		if (dao != null) {
			Empresa empresa = dao.findByCnpj(cnpj);
			return empresa == null;
		} else {
			// During LivrariaMvcApplication execution, there is no dependency injection
			return true;
		}

	}
}