## Grupo 8

Guilherme César Athayde - RA: 748175

Requisito: B1 - Sistema para oferta de vagas de estágios/empregos

# Como rodar a aplicação:

`mvn spring-boot:run`

Acessar http://localhost:8080

# Testes:

1. Faça login como administrador, acesse o painel de controle e cadastre um usuário do tipo Empresa e outro do tipo Profissional. Aproveite para testar as opções de editar e remover usuários. Depois, saia do sistema.

2. Entre com o usuário do tipo Profissional, localize uma vaga disponível e realize a candidatura. Em seguida, encerre a sessão.

3. Acesse o sistema como empresa, vá até a área de vagas e crie uma nova vaga. Após finalizar, faça logout.

4. Retorne ao sistema com o usuário da empresa, acesse a vaga criada e confira a lista de candidatos. O sistema deve permitir visualizar o perfil, baixar o currículo e alterar o status da candidatura caso o prazo de inscrição já tenha expirado.

5. Verifique se o usuário profissional recebeu um e-mail notificando sobre a mudança de status da candidatura.
