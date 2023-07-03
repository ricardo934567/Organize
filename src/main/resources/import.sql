-- Inserção das categorias
INSERT INTO categoria (nome) VALUES ("Supermercado");
INSERT INTO categoria (nome) VALUES ("Farmácia");
INSERT INTO categoria (nome) VALUES ("Academia");

-- Inserção das metas de categoria
INSERT INTO meta_categoria (limite, controle, categoria_id) VALUES (1000.0, 1, 1);

-- Inserção das faturas (3 faturas para cada categoria)
-- Categoria: Supermercado
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 3, 800.0, 1);
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 2, 400.0, 1);
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 1, 150.0, 1);

-- Categoria: Farmácia
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 4, 500.0, 2);
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 3, 300.0, 2);
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 1, 200.0, 2);

-- Categoria: Academia
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 2, 240.0, 3);
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 3, 400.0, 3);
INSERT INTO fatura (faturado, parcelas, valor_total, categoria_id) VALUES (0, 1, 120.0, 3);

-- Inserção das transações (parcelas) para cada fatura
-- Fatura 1 (Supermercado) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao (data_pagamento, data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-01 10:00:00','2023-06-01 10:00:00', '2023-06-01 10:00:00', 1, 266.66, 1);
INSERT INTO transacao (data_pagamento, data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-01 10:00:00','2023-06-01 10:00:00', '2023-06-01 10:00:00', 2, 266.66, 1);
INSERT INTO transacao (data_pagamento, data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-01 10:00:00','2023-06-01 10:00:00', '2023-06-01 10:00:00', 3, 266.68, 1);

-- Fatura 2 (Supermercado) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-05 10:00:00', '2023-06-05 10:00:00', 1, 200.0, 2);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-05 10:00:00', '2023-06-05 10:00:00', 2, 200.0, 2);

-- Fatura 3 (Supermercado) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-10 10:00:00', '2023-06-10 10:00:00', 1, 150.0, 3);

-- Fatura 4 (Farmácia) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-02 10:00:00', '2023-06-02 10:00:00', 1, 125.0, 4);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-02 10:00:00', '2023-06-02 10:00:00', 2, 125.0, 4);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-02 10:00:00', '2023-06-02 10:00:00', 3, 125.0, 4);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-02 10:00:00', '2023-06-02 10:00:00', 4, 125.0, 4);

-- Fatura 5 (Farmácia) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-06 10:00:00', '2023-06-06 10:00:00', 1, 100.0, 5);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-06 10:00:00', '2023-06-06 10:00:00', 2, 100.0, 5);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-06 10:00:00', '2023-06-06 10:00:00', 3, 100.0, 5);

-- Fatura 6 (Farmácia) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-12 10:00:00', '2023-06-12 10:00:00', 1, 200.0, 6);

-- Fatura 7 (Academia) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-03 10:00:00', '2023-06-03 10:00:00', 1, 120.0, 7);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-03 10:00:00', '2023-06-03 10:00:00', 2, 120.0, 7);

-- Fatura 8 (Academia) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-07 10:00:00', '2023-06-07 10:00:00', 1, 133.33, 8);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-07 10:00:00', '2023-06-07 10:00:00', 2, 133.33, 8);
INSERT INTO transacao ( data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-07 10:00:00', '2023-06-07 10:00:00', 3, 133.34, 8);

-- Fatura 9 (Academia) - Faturas abertas (parcelas não pagas)
INSERT INTO transacao ( data_pagamento,data_transacao, data_vencimento, parcela, valor, fatura_id)VALUES ( '2023-06-16 10:00:00','2023-06-15 10:00:00', '2023-06-15 10:00:00', 1, 120.0, 9);

