INSERT INTO jutsus_map (personagem_id, nome_jutsu, dano, consumo_de_chakra)
SELECT 
    p.id,
    j.jutsus,
    50,  
    20   
FROM personagem p
JOIN jutsus j ON p.id = j.personagem_id;

DROP TABLE IF EXISTS jutsus;