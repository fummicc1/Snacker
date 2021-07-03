package dev.fummicc1.lit.snacker.services

import android.util.Log
import org.jsoup.Jsoup
import java.lang.reflect.Executable

class HTMLClient() {

    fun getTitle(url: String): String {
        val document = Jsoup.connect(url).get()
        return document.title()
    }

    fun getOGPImageUrl(url: String): String? {

        try {
            val document = Jsoup.connect(url).get()

            val metaTag = document.head().getElementsByTag("meta")
            metaTag.forEach { tag ->
                Log.d("DomAnalyzer", "metatag: ${tag.tagName()}")
            }
            val image = metaTag
                .filter {
                    if (it.hasAttr("property").not()) {
                        return@filter false
                    }
                    val propertyValue = it.attr("property")
                    return@filter propertyValue.contains("og:image")
                }.firstOrNull()
            if (image == null) {

                document.getElementsByTag("img").forEach {
                    Log.d("DomAnalyzer", it.wholeText())
                }

                return null
            }
            val imageUrl = image.attr("content")
            return imageUrl
        } catch (e: Exception) {
            return null
        }
    }

}