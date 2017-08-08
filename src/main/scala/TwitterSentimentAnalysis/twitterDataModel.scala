/** This software is released under the University of Illinois/Research and Academic Use License. See
  * the LICENSE file in the root folder for details. Copyright (c) 2016
  *
  * Developed by: The Cognitive Computations Group, University of Illinois at Urbana-Champaign
  * http://cogcomp.cs.illinois.edu/
  */
package TwitterSentimentAnalysis

import java.net.{MalformedURLException, SocketTimeoutException}

import edu.illinois.cs.cogcomp.saul.datamodel.DataModel
import edu.illinois.cs.cogcomp.saulexamples.nlp.BaseTypes.{Document, Sentence, Token}
import sensors._
import twitter.datastructures.Tweet
import edu.illinois.cs.cogcomp.saulexamples.nlp.LanguageBaseTypeSensors._

import scala.collection.JavaConversions._

/** Created by guest on 10/2/16.
  */
object twitterDataModel extends DataModel {

  val tweet = node[Tweet]
  /*
  val documents = node[Document]
  val sentences = node[Sentence]
  val tokens =node[Token]

  val tweetToDoc = edge(tweet,documents)

   tweetToDoc.addSensor(generateDocFromTweet _)

  val docToSentence = edge(documents,sentences)
  docToSentence.addSensor(documentToSentenceGenerating _)

  val sentToToken = edge(sentences,tokens)

  sentToToken.addSensor(sentenceToTokenGenerating _)

   val tokenText = property(tokens) {
    x: Token => x.getText
  }

  val tokenFeatures = property (tweet) {
    x: Tweet =>
     val a =  (tweet(x) ~>tweetToDoc~>docToSentence~>sentToToken) prop tokenText
   a.toList
  }
 */

  val WordFeatures = property(tweet) {
    x: Tweet =>
      val a = x.getWords.toList
      a
  }


  val BigramFeatures = property(tweet) {
    x: Tweet => x.getWords.toList.sliding(2).map(_.mkString("-")).toList
  }

  val TRIPSFeatures = property(tweet) {
    x: Tweet => {
      val http_input = x.getText.replaceAll("@","").replaceAll("'","%27").split("\\s+").mkString("+");
      var result: List[String] = Nil
      try {
        val xml_string = get("http://trips.ihmc.us/parser/cgi/parse?input=" + http_input);
        val pattern = "(?<=<LF:indicator>F<\\/LF:indicator>\\n\\s{4}<LF:type>)(.*)(?=<\\/LF:type>)".r
        pattern.findAllIn(xml_string).foreach(println)
        Console.print()
        result = pattern.findAllIn(xml_string).toList;
      }catch{
        case ex: MalformedURLException => {
          Console.print("MalformedURLException\n");
        }
        case ex: SocketTimeoutException => {
          Console.print("SocketTimeoutException\n");
        }
      }
      result
    }
  }

  def get(url: String,
          connectTimeout: Int = 20000,
          readTimeout: Int = 20000,
          requestMethod: String = "GET") =
  {
    import java.net.{URL, HttpURLConnection}
    val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    val inputStream = connection.getInputStream
    val content = io.Source.fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close
    content
  }


  val Label = property(tweet) {
    x: Tweet => x.getSentimentLabel
  }
}
