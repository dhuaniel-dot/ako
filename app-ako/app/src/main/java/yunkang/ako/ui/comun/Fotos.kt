package yunkang.ako.ui.comun

import android.widget.ImageView
import com.bumptech.glide.Glide
import yunkang.ako.R

// Pinta en el hueco la foto de un plato o de una categoría. «foto» es un File (nuestro archivo en
// fotos/) o un String (la dirección content://… que prestó el selector); null = sin foto.
// Glide se llama siempre, también sin foto (null → el «?»), porque las vistas se reciclan y una
// que llevaba la foto de Entrecot la seguiría llevando al pintar otro plato.
// El lector de pantalla dice el nombre si hay foto y «Sin foto» si no
fun pintarFoto(hueco: ImageView, foto: Any?, nombre: String, redonda: Boolean = false, grande: Boolean = false) {
    val peticion = Glide.with(hueco)
        .load(foto)
        .placeholder(R.drawable.foto_cargando)
        .error(if (grande) R.drawable.ic_sin_foto_grande else R.drawable.ic_sin_foto)
    if (redonda) peticion.circleCrop().into(hueco) else peticion.centerCrop().into(hueco)
    hueco.contentDescription = if (foto == null) hueco.context.getString(R.string.comun_sin_foto_cd) else nombre
}
