import org.json.JSONObject

data class Weather(val weatherJson: JSONObject,var mainJson: JSONObject) {
    var id: Int = weatherJson.getInt("id")
    var main: String = weatherJson.getString("main")
    var description: String = weatherJson.getString("description")
    var icon: String = weatherJson.getString("icon")

}