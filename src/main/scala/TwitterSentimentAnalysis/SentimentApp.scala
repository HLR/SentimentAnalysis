/** This software is released under the University of Illinois/Research and Academic Use License. See
  * the LICENSE file in the root folder for details. Copyright (c) 2016
  *
  * Developed by: The Cognitive Computations Group, University of Illinois at Urbana-Champaign
  * http://cogcomp.cs.illinois.edu/
  */
package TwitterSentimentAnalysis

import java.io.PrintWriter
import java.net.{MalformedURLException, SocketTimeoutException, URLEncoder}

import TwitterSentimentAnalysis.twitterClassifiers.{sentimentClassifier, sentimentClassifier2}
import twitter.datastructures.Tweet
import twitter.tweet.TweetReader
import twitterDataModel._

import scala.collection.JavaConversions._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/** Created by guest on 10/2/16.
  */
object SentimentApp extends App {


  val TrainReader = new TweetReader("./data/twitter/train50k.csv.gz")
  val TestReader = new TweetReader("./data/twitter/test.csv.gz")

  tweet.populate(TrainReader.tweets.toList)
  tweet.populate(TestReader.tweets.toList, train = false)
  sentimentClassifier.learn(1)
  sentimentClassifier.test()
  //sentimentClassifier.crossValidation(3)
  //sentimentClassifier2.classifier.discreteValue(new Tweet("here is my tweet."))
  sentimentClassifier.save()
}

object downloadResults extends App {

  val trainReader = new TweetReader("./data/twitter/train50k.csv.gz")
  val testReader = new TweetReader("./data/twitter/test.csv.gz")
  val threadNumber = 100
  val threads = Range(0, threadNumber).map(i=> {
    val thread = new Thread {
      override def run {
        println(s"Thread $i started ...")
        val writer = new PrintWriter(s"./data/twitter/trips_${i}.txt")

        val blockSize = trainReader.tweets.size() / threadNumber + 1
        writer.println(s"from tweet number ${i * blockSize} to ${(i + 1) * blockSize + 1}")
        writer.println()
        trainReader.tweets.toList.slice(i * blockSize, (i + 1) * blockSize + 1).foreach(x => {
          val http_input = x.getText.replaceAll("@", "")
          var result: List[String] = Nil
          try {
            println(s"$i -- getting features for $http_input")
            val xml_string = get("http://trips.ihmc.us/parser/cgi/parse?input=" + URLEncoder.encode(http_input, "UTF-8"));
            val pattern = "(?<=<LF:indicator>F<\\/LF:indicator>\\n\\s{4}<LF:type>)(.*)(?=<\\/LF:type>)".r
            // pattern.findAllIn(xml_string).foreach(println)
            // Console.print()
            result = pattern.findAllIn(xml_string).toList
            writer.println(s"${x.getText.hashCode} [${result.mkString(",")}]")
            writer.flush()
            println(s"$i -- success for $http_input")

          } catch {
            case ex: MalformedURLException => {
              println(s"$i -- MalformedURLException");
            }
            case ex: SocketTimeoutException => {
              println(s"$i -- SocketTimeoutException");
            }
            case x => {
              println(s"$i -- ${x.getMessage}");
            }
          }
        })
        println(s"Thread $i finished.")
      }
    }
    thread
  }).toList

  threads.foreach(_.start)
  threads.foreach(_.join)

  println("done.")


  def get(url: String,
          connectTimeout: Int = 20000,
          readTimeout: Int = 20000,
          requestMethod: String = "GET"): String = {
    import java.net.{URL, HttpURLConnection}
    val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    val inputStream = connection.getInputStream
    val content = scala.io.Source.fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close
    content
  }

}
