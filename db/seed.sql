-- Marcas
INSERT INTO marca (nomemarca, logo_url) VALUES
  ('Fiat', 'logos/fiat.png'),
  ('Volkswagen', 'logos/volkswagen.png'),
  ('Chevrolet', 'logos/chevrolet.png'),
  ('Ford', 'logos/ford.png'),
  ('Toyota', 'logos/toyota.png'),
  ('Honda', 'logos/honda.png'),
  ('Hyundai', 'logos/hyundai.png'),
  ('Nissan', 'logos/nissan.png'),
  ('Renault', 'logos/renault.png'),
  ('Jeep', 'logos/jeep.png');

-- Modelos (Fiat)
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Uno', 1), ('Palio', 1), ('Strada', 1), ('Toro', 1), ('Mobi', 1);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Gol', 2), ('Polo', 2), ('T-Cross', 2), ('Saveiro', 2), ('Amarok', 2);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Onix', 3), ('Prisma', 3), ('S10', 3), ('Tracker', 3), ('Cruze', 3);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Ka', 4), ('Fiesta', 4), ('Ranger', 4), ('EcoSport', 4), ('Focus', 4);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Corolla', 5), ('Hilux', 5), ('Etios', 5), ('Yaris', 5), ('SW4', 5);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Civic', 6), ('Fit', 6), ('HR-V', 6), ('City', 6), ('CR-V', 6);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('HB20', 7), ('Creta', 7), ('Tucson', 7), ('IX35', 7), ('Santa Fe', 7);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Kicks', 8), ('Sentra', 8), ('Frontier', 8), ('March', 8), ('Versa', 8);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Sandero', 9), ('Duster', 9), ('Kwid', 9), ('Captur', 9), ('Oroch', 9);
INSERT INTO modelo (nomemodelo, idmarca) VALUES ('Renegade', 10), ('Compass', 10), ('Cherokee', 10), ('Wrangler', 10), ('Grand Cherokee', 10);

-- Funções
INSERT INTO funcao (especialidade, comissao, funcaocolaborador) VALUES
  ('Motor e Suspensão', 5.00, 'Mecânico'),
  ('Sistemas Elétricos', 5.00, 'Eletricista'),
  ('Lataria e Pintura', 6.00, 'Funileiro'),
  ('Atendimento', 0.00, 'Recepcionista'),
  ('Administrativa', 2.00, 'Gerente'),
  ('Vendas', 3.00, 'Vendedor'),
  ('Serviços Gerais', 0.00, 'Auxiliar');

-- Fornecedores
INSERT INTO fornecedor (razaosocial, cnpj, ddi, ddd, numerofornecedor, email, enderecofornecedor, bairrofornecedor, cidadefornecedor, estadofornecedor, cepfornecedor) VALUES
  ('Auto Peças Brasil Ltda', '11222333000181', '55', '11', '22223333', 'vendas@autopecasbrasil.com.br', 'Rua das Peças, 100', 'Centro', 'São Paulo', 'SP', 1001001),
  ('Distribuidora de Peças ABC Ltda', '44555666000199', '55', '21', '22224444', 'contato@distribuicaoabc.com.br', 'Av. Central, 500', 'Centro', 'Rio de Janeiro', 'RJ', 20040001),
  ('Pneus Rodabem S.A.', '77888999000177', '55', '31', '22225555', 'comercial@rodabem.com.br', 'Rodovia BR-101, Km 50', 'Industrial', 'Belo Horizonte', 'MG', 30000001),
  ('Óleos e Lubrificantes Premium Ltda', '11122333000111', '55', '41', '22226666', 'pedidos@oleospremium.com.br', 'Rua do Óleo, 200', 'Jardim Amália', 'Curitiba', 'PR', 80010001),
  ('Ferragens Auto Center Ltda', '22233444000122', '55', '51', '22227777', 'sac@ferragensauto.com.br', 'Av. dos Ferros, 300', 'São Geraldo', 'Porto Alegre', 'RS', 90010001);

-- Pessoas (clientes PF)
INSERT INTO pessoa (nome, ddi1, ddd1, numerotelefone1, email, endereco, bairro, cidade, estado, cep, datacadastro) VALUES
  ('Carlos Alberto Silva', '55', '27', '988880001', 'carlos.silva@email.com', 'Rua das Flores, 150', 'Centro', 'Vitória', 'ES', 29010001, '2023-01-15'),
  ('Fernanda Oliveira Souza', '55', '11', '977770002', 'fernanda.souza@email.com', 'Av. Paulista, 1000', 'Bela Vista', 'São Paulo', 'SP', 1310001, '2023-06-01'),
  ('Pedro Henrique Santos', '55', '27', '966660003', 'pedro.santos@email.com', 'Rua XV de Novembro, 50', 'Praia do Canto', 'Vitória', 'ES', 29050001, '2023-03-20');

-- Pessoas Físicas
INSERT INTO pessoafisica (cpf, rg, datanascimento, idpessoa) VALUES
  ('12345678901', '1234567-ES', '1985-03-15', 1),
  ('23456789012', '2345678-SP', '1990-07-22', 2),
  ('34567890123', '3456789-ES', '1978-11-08', 3);

-- Clientes PF
INSERT INTO cliente (statuscliente, observacoes, idpessoafisica) VALUES
  ('Ativo', 'Cliente desde 2023', 1),
  ('Ativo', NULL, 2),
  ('Ativo', NULL, 3);

-- Pessoas (clientes PJ)
INSERT INTO pessoa (nome, ddi1, ddd1, numerotelefone1, email, endereco, bairro, cidade, estado, cep, datacadastro) VALUES
  ('Transportadora Rápida Ltda', '55', '27', '955550004', 'contato@transportadorarapida.com.br', 'Av. Beira Mar, 500', 'Praia da Costa', 'Vila Velha', 'ES', 29100001, '2024-01-10'),
  ('Auto Elétrica Central ME', '55', '27', '944440005', 'contato@autoeletricacentral.com.br', 'Rua das Ofinas, 200', 'Jardim América', 'Serra', 'ES', 29160001, '2024-02-01');

-- Pessoas Jurídicas
INSERT INTO pessoajuridica (cnpj, inscricaoestadual, razaosocial, idpessoa) VALUES
  ('11222333000181', '123456789', 'Transportadora Rápida Ltda', 4),
  ('44555666000199', '987654321', 'Auto Elétrica Central ME', 5);

-- Clientes PJ
INSERT INTO cliente (statuscliente, observacoes, idpessoajuridica) VALUES
  ('Ativo', 'Transportadora terceirizada', 1),
  ('Ativo', NULL, 2);

-- Pessoas (colaboradores)
INSERT INTO pessoa (nome, ddi1, ddd1, numerotelefone1, email, endereco, bairro, cidade, estado, cep, datacadastro) VALUES
  ('Admin Sistema', '55', '27', '999990001', 'admin@avcar.com.br', 'Rua Admin, 1', 'Centro', 'Vitória', 'ES', 29010001, '2022-01-01'),
  ('João Mecânico', '55', '27', '999990002', 'joao.mecanico@avcar.com.br', 'Rua João, 100', 'Praia do Canto', 'Vitória', 'ES', 29050001, '2023-03-15'),
  ('Maria Recepcionista', '55', '27', '999990003', 'maria.recepcao@avcar.com.br', 'Rua Maria, 50', 'Jardim da Penha', 'Vitória', 'ES', 29060001, '2023-06-01');

-- Colaboradores
INSERT INTO colaborador (matricula, cpf, dataadmissao, salario, observacoes, idpessoa, ativo) VALUES
  ('COL001', '00000000191', '2022-01-01', 5000.00, NULL, 6, TRUE),
  ('COL002', '11122233344', '2023-03-15', 3200.00, NULL, 7, TRUE),
  ('COL003', '55566677788', '2023-06-01', 1800.00, NULL, 8, TRUE);

-- Colaborador_Funcao
INSERT INTO colaborador_funcao (idcolaborador, idfuncao) VALUES (1, 5);
INSERT INTO colaborador_funcao (idcolaborador, idfuncao) VALUES (2, 1);
INSERT INTO colaborador_funcao (idcolaborador, idfuncao) VALUES (2, 7);
INSERT INTO colaborador_funcao (idcolaborador, idfuncao) VALUES (3, 4);

-- Veículos
INSERT INTO veiculo (placa, chassi, anofabricacao, anomodelo, cor, quilometragem, acessorios, idmodelo, ativo) VALUES
  ('ABC1234', '9BD11111111111111', 2019, 2020, 'Prata', 45000, 'Ar condicionado, Direção hidráulica', 1, TRUE),
  ('DEF5678', '9BD22222222222222', 2020, 2020, 'Preto', 35000, 'Vidros elétricos, Travas elétricas', 6, TRUE),
  ('GHI9012', '9BD33333333333333', 2018, 2019, 'Branco', 60000, 'Ar condicionado', 2, TRUE);

-- Histórico Cliente-Veículo
INSERT INTO historicocliente (idveiculo, idcliente, datainicio) VALUES
  (1, 1, '2023-01-15'),
  (2, 2, '2023-06-01'),
  (3, 3, '2023-03-20');

-- Peças
INSERT INTO pecas (codigonacional, codigointernopeca, nomepeca, descricaopeca, fabricantepeca, categoriapeca, valorcustopeca, valorvendapeca, quantidadeestoque, datacomprapeca, garantiapeca, ativo) VALUES
  (1001, 'PST-FREIO-001', 'Pastilha de Freio Dianteira', 'Pastilha de freio dianteira para veículos compactos', 'Bosch', 'Freio', 45.00, 89.90, 50, '2024-01-10', 180, TRUE),
  (1002, 'OLEO-5W30-001', 'Óleo Motor 5W30', 'Óleo lubrificante sintético 5W30 1L', 'Shell', 'Lubrificante', 25.00, 49.90, 100, '2024-01-15', 365, TRUE),
  (1003, 'FLT-OLEO-001', 'Filtro de Óleo', 'Filtro de óleo automotivo padrão', 'Mann', 'Filtro', 12.00, 29.90, 80, '2024-02-01', 180, TRUE),
  (1004, 'COR-DENT-001', 'Correia Dentada', 'Correia dentada 1.0/1.4', 'Gates', 'Motor', 35.00, 79.90, 30, '2024-02-10', 365, TRUE),
  (1005, 'BAT-60AH-001', 'Bateria 60Ah', 'Bateria automotiva 60Ah 12V', 'Moura', 'Elétrica', 180.00, 349.90, 20, '2024-03-01', 365, TRUE);

-- Fornecedor_Peças
INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (1, 1);
INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (4, 2);
INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (1, 3);
INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (1, 4);
INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (2, 5);

-- Serviços
INSERT INTO servico (nomeservico, descricaoservico, valorservico, garantiadias, tempoestimado, ativo) VALUES
  ('Troca de Óleo', 'Troca de óleo do motor com filtro', 80.00, 90, '30 min', TRUE),
  ('Alinhamento', 'Alinhamento de direção', 60.00, 90, '30 min', TRUE),
  ('Balanceamento', 'Balanceamento de rodas', 50.00, 90, '20 min', TRUE),
  ('Troca de Pastilhas de Freio', 'Substituição das pastilhas de freio dianteiras', 120.00, 180, '60 min', TRUE),
  ('Revisão Completa', 'Revisão geral de 50 itens do veículo', 350.00, 90, '120 min', TRUE),
  ('Troca de Correia Dentada', 'Substituição da correia dentada', 200.00, 180, '90 min', TRUE),
  ('Troca de Bateria', 'Substituição da bateria', 50.00, 90, '20 min', TRUE),
  ('Limpeza de Bicos', 'Limpeza ultrassônica de bicos injetores', 180.00, 90, '60 min', TRUE);

-- Ordens de Serviço
INSERT INTO ordemservico (numeroos, entradaveiculo, dataabertura, datafechamento, defeitorelatado, quantidadepecas, valortotalpecas, valormaodeobra, valorservicoexterno, formadepagamento, valordesconto, valortotal, garantia, status, idveiculo, idcolaborador) VALUES
  (1, '2024-06-01', '2024-06-01', '2024-06-01', 'Óleo vencendo, filtro sujo', 2, 79.80, 80.00, 0, 'Dinheiro', 0, 159.80, 90, 'Finalizada', 1, 2),
  (2, '2024-06-10', '2024-06-10', '2024-06-10', 'Freios rangendo', 1, 89.90, 120.00, 0, 'Cartão', 0, 209.90, 180, 'Finalizada', 1, 2),
  (3, '2024-07-01', '2024-07-01', NULL, 'Revisão geral para viagem', 0, 0, 350.00, 0, NULL, 0, 350.00, 90, 'Aguardando peça', 2, 3),
  (4, '2024-07-15', '2024-07-15', NULL, 'Óleo baixo e motor irregular', 0, 0, 80.00, 0, NULL, 0, 80.00, 90, 'Aberta', 3, 3);

-- Itens_Servico_Oficina
INSERT INTO itensservicooficina (quantidadeitenservico, valorunitarioitenservico, valortotalitenservico, garantiadias, idcolaborador, idservico, idordemservico, horainicio, horafim, statusitenservico) VALUES
  (1, 80.00, 80.00, 90, 2, 1, 1, '2024-06-01 08:00:00', '2024-06-01 08:30:00', 'CONCLUIDO'),
  (1, 120.00, 120.00, 180, 2, 4, 2, '2024-06-10 08:00:00', '2024-06-10 09:20:00', 'CONCLUIDO'),
  (1, 350.00, 350.00, 90, 2, 5, 3, '2024-07-01 08:00:00', NULL, 'EM_ANDAMENTO'),
  (1, 80.00, 80.00, 90, 3, 1, 4, NULL, NULL, 'PENDENTE');

-- Itens_Peça
INSERT INTO itempecas (quantidade, valorunitario, valortotal, garantia, idpecas, idordemservico) VALUES
  (1, 49.90, 49.90, 180, 2, 1),
  (1, 29.90, 29.90, 180, 3, 1),
  (1, 89.90, 89.90, 180, 1, 2);

-- Parceiro Externo
INSERT INTO parceiro_externo (nome, cnpj, tipo_servico, telefone, email, ativo) VALUES
  ('Funilaria Rápida', '99888777000155', 'Funilaria e Pintura', '2733331000', 'contato@funilariarapida.com.br', TRUE),
  ('Guincho 24h', '77666555000144', 'Guincho e Reboque', '2733332000', 'guincho24h@email.com', TRUE),
  ('Auto Vidros', '55444333000133', 'Troca de Vidros', '2733333000', 'contato@autovidros.com.br', TRUE);
