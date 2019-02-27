package net.sansa_stack.rdf.common.test.partition.core

import net.sansa_stack.rdf.common.partition.core.RdfPartitionerDefault._
import org.apache.jena.graph.{ Node, NodeFactory, Triple }
import org.scalatest.FunSuite
import net.sansa_stack.rdf.common.partition.core.RdfPartitionDefault
import net.sansa_stack.rdf.common.partition.schema.SchemaStringStringLang
import net.sansa_stack.rdf.common.partition.schema.SchemaStringString

class RdfPartitionerDefaultTests extends FunSuite {

  val triple = Triple.create(
    NodeFactory.createURI("http://dbpedia.org/resource/Guy_de_Maupassant"),
    NodeFactory.createURI("http://xmlns.com/foaf/0.1/givenName"),
    NodeFactory.createLiteral("Guy De"))

  test("getting URI or BNode string should match") {
    val node = NodeFactory.createURI("http://dbpedia.org/resource/Guy_de_Maupassant")
    assert(getUriOrBNodeString(triple.getSubject).matches(node.getURI))
  }

  test("getting RDF Term type should match") {
    assert(getRdfTermType(triple.getSubject) == 1)
  }

  test("chechking if data type is PlainLiteral should match") {
    assert(isPlainLiteralDatatype(triple.getObject.getLiteralDatatypeURI))
  }

  test("chechking if [[Node]] is TypedLiteral should match") {
    assert(!isTypedLiteral(triple.getObject))
  }

  test("getting partitioning layout from [[Triple]] should match") {
    val expectedPartition = RdfPartitionDefault(1, "http://xmlns.com/foaf/0.1/givenName",
      2, "http://www.w3.org/2001/XMLSchema#string", true)
    assert(fromTriple(triple).equals(expectedPartition))
  }

  test("determining Layout should match") {
    val expectedLayout = SchemaStringStringLang("http://dbpedia.org/resource/Guy_de_Maupassant", "Guy De", "")
    assert(determineLayout(fromTriple(triple)).fromTriple(triple).equals(expectedLayout))
  }

  test("determining Layout Datatype should match") {
    val expectedLayoutDatatype = SchemaStringString("http://dbpedia.org/resource/Guy_de_Maupassant", "Guy De")
    assert(determineLayoutDatatype(triple.getObject.getLiteralDatatypeURI).fromTriple(triple).equals(expectedLayoutDatatype))
  }
}
