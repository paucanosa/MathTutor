package furhatos.app.spacereceptionist.nlu;


import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import java.util.*


fun getSentiment(txt: String): String? {
//    Sva sent = new Sentence("I don't like math");
    val props = Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")
    val pipeline = StanfordCoreNLP(props)

    val annotation = pipeline.process(txt)
    val sentences = annotation.get(SentencesAnnotation::class.java)
    for (sentence in sentences) {
        val sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass::class.java)
//        println(sentiment + "\t" + sentence)
        return sentiment
    }
    return null
}

