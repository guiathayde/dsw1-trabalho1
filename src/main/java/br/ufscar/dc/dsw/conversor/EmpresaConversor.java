package br.ufscar.dc.dsw.conversor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;

@Component
public class EmpresaConversor implements Converter<String, Company> {

  @Autowired
  private IEmpresaService service;

  @Override
  public Company convert(String text) {
    if (text.isEmpty()) {
      return null;
    }
    Long id = Long.valueOf(text);
    return service.buscarPorId(id);
  }
}