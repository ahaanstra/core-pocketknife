package com.semantica.pocketknife;

import java.io.Serializable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.semantica.pocketknife.WrappedSerializable.SerializationType;

public class WrappedSerializableTest {

	class SomeSerializable implements Serializable {
		private static final long serialVersionUID = 1L;
		private String myValue = "test";

		public String getMyValue() {
			return myValue;
		}

		public void setMyValue(String myValue) {
			this.myValue = myValue;
		}

	}

	@Test
	public void toStringShouldSerializeToJson() throws JsonProcessingException {
		WrappedSerializable<SomeSerializable> test = WrappedSerializable.builder(new SomeSerializable())
				.defaultSerializationType(SerializationType.JSON).build();
		String expectedJson = "{\"myValue\":\"test\"}";
		assert test.getJson().equals(expectedJson);
		assert test.toString().equals(expectedJson);
	}

	@Test
	public void toStringShouldSerializeToYAML() throws JsonProcessingException {
		WrappedSerializable<SomeSerializable> test = WrappedSerializable.builder(new SomeSerializable())
				.defaultSerializationType(SerializationType.YAML).build();
		String expectedYaml = "---\n" + "myValue: \"test\"\n";
		Assertions.assertEquals(expectedYaml, test.getYaml());
		Assertions.assertEquals(expectedYaml, test.toString());
	}

	@Test
	public void inputAndOutputObjectShouldBeTheSame() {
		SomeSerializable serializable = new SomeSerializable();
		WrappedSerializable<SomeSerializable> test = WrappedSerializable.builder(serializable).build();
		assert test.getObject() == serializable;
	}

	@Test
	public void shouldThrowExceptionWhenTryingToSerializeBeanWithoutProperties() throws JsonProcessingException {
		Serializable serializable = new Serializable() {
			private static final long serialVersionUID = 1L;
		};
		WrappedSerializable<Serializable> test = WrappedSerializable.builder(serializable)
				.defaultSerializationType(SerializationType.YAML).build();
		Assertions.assertThrows(IllegalStateException.class, test::toString);
	}

}
