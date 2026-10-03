package com.examen.apirestfull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest        
@AutoConfigureMockMvc  
class ApirestfullApplicationTests {

	//Para enviar peticiones simuladas a la API (Spring la inyecta solo)
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Prueba el recorrido completo del CRUD:
	 * listar, crear, obtener, actualizar, eliminar y los errores 404 y 400.
	 */
	@Test
	void crudDeProductosYErroresHttp() throws Exception {
		// JSON de un producto valido, que se reutiliza en el POST y en el PUT
		String producto = """
				{
				  "nombre": "Cafe",
				  "categoria": "Almacen",
				  "cantidad": 12,
				  "stockMinimo": 4,
				  "precio": 3200.00
				}
				""";

		// GET /api/productos:responde 200 y la lista tiene al menos un producto con id
		mockMvc.perform(get("/api/productos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").exists());

		//  POST /api/productos: crea el producto y responde 201 (Created)
		//    Se guarda la respuesta para poder leer el id que genero la base de datos
		String respuesta = mockMvc.perform(post("/api/productos")
					.contentType(MediaType.APPLICATION_JSON)
					.content(producto))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nombre").value("Cafe"))
				.andReturn().getResponse().getContentAsString();

		// Se extrae el id del JSON de respuesta para usarlo en las pruebas siguientes
		long id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
				.readTree(respuesta).get("id").asLong();

		// GET /api/productos/{id}:  obtiene el producto recien creado (200)
		mockMvc.perform(get("/api/productos/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Cafe"));

		// PUT /api/productos/{id}: actualiza el nombre a "Cafe molido" (200)
		mockMvc.perform(put("/api/productos/{id}", id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(producto.replace("Cafe", "Cafe molido")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Cafe molido"));

		// DELETE /api/productos/{id}: elimina el producto (204 sin contenido)
		mockMvc.perform(delete("/api/productos/{id}", id))
				.andExpect(status().isNoContent());

		// GET del producto eliminado: ya no existe, responde 404 con status 404 en el JSON
		mockMvc.perform(get("/api/productos/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));

		//  POST con datos invalidos (nombre vacio, cantidad y precio negativos):
		//  responde 400 e incluye el detalle de los campos que fallaron
		mockMvc.perform(post("/api/productos")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"nombre\":\"\",\"cantidad\":-1,\"stockMinimo\":2,\"precio\":-1}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detalles").exists());
	}

	/**
	 * Prueba el endpoint de stock bajo: tiene que devolver los productos sin stock
	 * y los que estan en el minimo o por debajo, segun los datos de data.sql.
	 */
	@Test
	void stockBajoIncluyeProductosEnElMinimoYSinStock() throws Exception {
		mockMvc.perform(get("/api/productos/stock-bajo"))
				.andExpect(status().isOk())
				// containsInAnyOrder: tienen que estar exactamente estos 3 nombres, en cualquier orden
				.andExpect(jsonPath("$[*].nombre").value(containsInAnyOrder(
						"Leche entera 1L", "Detergente 750ml", "Jabon de tocador")));
	}

	/**
	 * Prueba que eliminar un id que no existe devuelve 404 (y no un error del servidor).
	 */
	@Test
	void eliminarProductoInexistenteDevuelve404() throws Exception {
		// Long.MAX_VALUE es un id imposible de encontrar en la base
		mockMvc.perform(delete("/api/productos/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

}