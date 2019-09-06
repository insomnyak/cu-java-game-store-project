use game_store;
use game_store_test;

insert into processing_fee (product_type, fee) values 
('Consoles',14.99),
('T-Shirts',1.98),
('Games',1.49);

select * from processing_fee;

show columns from processing_fee;