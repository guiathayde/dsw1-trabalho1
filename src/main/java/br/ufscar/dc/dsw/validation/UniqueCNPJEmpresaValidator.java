package br.ufscar.dc.dsw.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.domain.Empresa;

@Component
public class UniqueCNPJEmpresaValidator implements ConstraintValidator<UniqueCNPJEmpresa, Empresa> {

	@Autowired
	private IEmpresaDAO dao;

	@Override
	public boolean isValid(Empresa empresa, ConstraintValidatorContext context) {
		if (dao != null) {
			Empresa empresaExistente = dao.findByCnpj(empresa.getCnpj());
			return empresaExistente == null || empresaExistente.getId().equals(empresa.getId());
		} else {
			return true;
		}

	}
}