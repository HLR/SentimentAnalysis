package TwitterSentimentAnalysis

import edu.illinois.cs.cogcomp.saulexamples.nlp.BaseTypes.Document
import twitter.datastructures.Tweet

/**
  * Created by parisakordjamshidi on 6/20/17.
  */
object sensors {
  def generateDocFromTweet (t:Tweet): Document =
    new Document(t.hashCode().toString, -1, -1, t.getText)
}
