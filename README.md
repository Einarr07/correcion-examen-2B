<h1>C.R.U.D vehículos(carros)</h1>
<hr>
<h3>Spript BD</h3>
<hr>
<p> 
Create database Vehiculos;

use Vehiculos;

create table carros( placa int primary key not null, 
marca varchar (25) not null, 
modelo varchar (25) not null, 
color varchar (25) not null, 
uso varchar (25) not null, 
gasolina varchar (25) not null, 
pais varchar (25) not null );

create table estadoVehiculo(
id int auto_increment not null,
Caracteristica varchar(25) not null,
primary key(id)
);

Insert into estadoVehiculo (id, Caracteristica) values (null, "Nuevo"), (null, "Semi-Nuevo"), (null, "Semi-Usado"), (null, "Usado");
SELECT * FROM estadoVehiculo;

create table gasolina(
id int primary key auto_increment not null,
Nombre_Gasolina varchar(10)
);

INSERT INTO gasolina (id, Nombre_Gasolina) values (null, "Super"), (null, "Extra");
SELECT * FROM gasolina;

create table pais(
id int primary key auto_increment not null,
Pais varchar(25) not null
);

INSERT INTO pais (id, Pais) values (null, "Ecuador"), (null, "Colombia"), (null, "Venezuela"), (null, "Peru"), (null, "Chile"), (null, "Argentina"), (null, "Canada"), (null, "Mexico"), (null, "Brazil"), (null, "Panama"), (null, "Bolivia");
</p>

<hr>
<h3>Video del funcionamiento de la aplicación</h3>

https://user-images.githubusercontent.com/96399138/220817629-8014e426-e0b4-4a5f-a509-197c43cd5796.mp4



