INSERT INTO categoria(nome) VALUES ("Supermercado");
INSERT INTO categoria(nome) VALUES ("Farmácia");
INSERT INTO categoria(nome) VALUES ("Academia");

INSERT INTO meta_categoria(limite,controle,categoria_id) VALUES (2,1,1);

INSERT INTO fatura(faturado,parcelas,valor_total,categoria_id) VALUES (true,3,250.00,1);

INSERT INTO transacao(data_pagamento,data_transacao,data_vencimento,parcela,valor,fatura_id) VALUES (utc_timestamp,DATE_ADD(CURDATE(), INTERVAL 30 DAY),DATE_ADD(CURDATE(), INTERVAL 45 DAY),3,250.00,1);