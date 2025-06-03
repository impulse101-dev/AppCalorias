package com.example.appcalorias.api.ollama.response.post.foodProperties

import android.util.Log
import com.example.appcalorias.api.ollama.response.post.PostResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.Locale

/**
 * Esta es la clase que se encarga de manejar los datos de la respuesta de la api y poder
 * sacar los datos de forma estructurada.
 *
 * Dado a que las respuestas de la api son tan poco fiables, era necesario crear esta clase para poder
 * obtener respuestas de forma mas estable, realizando calculos en casos en los que falte alguna de las
 * propiedades.
 *
 * @param postResponse la respuesta de la api, que contiene el json con las propiedades de la comida.
 * @property response la respuesta de la api en formato String, que contiene las propiedades de la comida. (Sale del PostResponse)
 * @author Adrian Salazar Escoriza
 */
class FoodPropertiesManager(postResponse : PostResponse) {
    val response : String = postResponse.response

    /**
     * Este metodo se encarga de obtener las propiedades de la comida a partir de la respuesta de la api
     * mediante el uso de la libreria com.google.Gson para parsear el json que devuelve la api.
     * @return Devuelve un objeto de tipo FoodProperties que contiene las propiedades de la comida ya
     * parseadas y filtradas.
     */
    fun getFoodProperties() : FoodProperties {
        val jsonElement = JsonParser.parseString(response)
        val jsonObject = jsonElement.asJsonObject


        val calories = getPropertyOrDefault(jsonObject, "calories")
        val carbohydrates = getPropertyOrDefault(jsonObject, "carbohydrates")
        val proteins = getPropertyOrDefault(jsonObject, "proteins")
        val fats = getPropertyOrDefault(jsonObject, "sugars")   //el cambio dado a que el modelo de vision no es capaz de dar los lipidos

        return caloriesFilter(
            FoodProperties(
                calories = calories,
                carbohydrates = carbohydrates,
                protein = proteins,
                fat = fats
            )
        )
    }


    /**
     * Este metodo es necesario, dado a que los modelos multimodales que he estado probando, no siempre devuelven el resultado
     * esperado. En ese caso, para que la aplicacion no este fallando cada rato, se le puede asignar un valor por defecto si
     * la respuesta de la api no es la esperada.
     * @param jsonObject el objeto json que contiene la respuesta de la api
     * @param property la propiedad a mapear
     * @param default el valor por defecto a asignar
     * @return el valor de la propiedad o el valor por defecto
     */
    private fun getPropertyOrDefault (jsonObject: JsonObject, property: String, default: String = "0") : String {
        return if (jsonObject.has(property) && !jsonObject.get(property).isJsonNull)
            jsonObject.get(property).asString
        else
            default
    }


    /**
     * Dado a que la respuesta de la api no siempre podria contener las propiedades del plato, este
     * metodo se encarga de realizar una estimacion segun los otros valores. (En caso de no tenerlos).
     * @param foodProperties Propiedades de la comida.
     * @return Devuelve el valor pasado por parametro, en caso de que las propiedades ya sean adecuadas, no se realizaran cambios a estas.
     */
    private fun caloriesFilter(foodProperties: FoodProperties) : FoodProperties {
        var totalCalories = foodProperties.calories.toFloat()
        if (totalCalories <= 0) {
            totalCalories = ((foodProperties.carbohydrates.toFloat() * 4) + (foodProperties.protein.toFloat() * 4) + (foodProperties.fat.toFloat() * 9))
            Log.d("caloriesRawValue", "calories: $totalCalories")
            return FoodProperties(
                calories = String.format(Locale.US,"%.1f", totalCalories),
                carbohydrates = String.format(Locale.US,"%.1f", foodProperties.carbohydrates.toFloat()),
                protein = String.format(Locale.US,"%.1f", foodProperties.protein.toFloat()),
                fat = String.format(Locale.US,"%.1f", foodProperties.fat.toFloat())
            )
        }
        return foodProperties
    }
}