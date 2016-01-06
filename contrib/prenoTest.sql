--prenotazione in data odierna in cui compare l'utente
select 
p.id,
p.utente
,pg.nome campo
,b.data
,b.ora 
from persona p 
inner join socio u on u.persona_id = p.id 
inner join campo pg on pg.societa_id =  u.societa_id 
inner join preno b on b.socio_id = u.id and pg.id = b.campo_id and b.data = CURDATE()
where 
b.campo_id in (
	SELECT distinct pg.id
	from persona p 
	inner join socio u on u.persona_id = p.id 
	inner join campo pg on pg.societa_id =  u.societa_id 
	inner join preno b on b.socio_id = u.id and pg.id = b.campo_id and b.data = CURDATE()
	where p.id = (
		----minimno utente che ha almeno una prenotazione nella data
		select 
		min(p.id)
		from persona p 
		inner join socio u on u.persona_id = p.id 
		inner join campo pg on pg.societa_id =  u.societa_id 
		inner join preno b on b.socio_id = u.id and pg.id = b.campo_id and b.data = CURDATE()
		) 
)
order by pg.sequenza, pg.id, b.ora