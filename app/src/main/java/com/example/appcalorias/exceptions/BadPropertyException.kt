package com.example.appcalorias.exceptions

/**
 * Excepcion lanzada cuando hay un error en las propiedades del archivo de configuracion
 * @author Adrian Salazar Escoriza
 */
class BadPropertyException(
    message : String = "Error en las propiedades del archivo de configuracion \"properties.conf\""
) : RuntimeException(message) {

}