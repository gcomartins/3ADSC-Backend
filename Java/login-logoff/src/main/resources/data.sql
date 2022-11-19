insert into usuario
(nome, email, is_autenticado, cpf,senha,data_nascimento)
values
('eduardo', 'eduardo@test.com',false, '50422211826','eduardo123456','20020603');

insert into objetivo
(categoria, data, descricao,nome,valor,data_final,valor_atual,usuario_id)
values
('viagem','20240727','viagem para a europa','Final da champions 2024',10000.00,'20240727',1000.00,1),
('viagem2','20240719','viagem para a europa','Semifinal da champions 2024',8000.00,'20240719',500.00,1);

insert into receita
(categoria, data, descricao,nome,valor,frequencia,is_recorrente,usuario_id)
values
('Salario','20240101','Salario do emprego fixo', 'Salario',5000.00,1,true,1);

insert into despesa
(categoria, data, descricao,nome,valor,is_pago,qtd_parcelas,usuario_id)
values
('automoveis','20241027','parcela do carro','golf',545.50,false,10,1);