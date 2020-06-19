package com.semantica.pocketknife;

import java.io.Serializable;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * This class functions to wrap a test data object and provide an easy way to
 * check and compare their json and yaml contents. Also it overrides toString,
 * returning the object serialized to the default type. This is particulary
 * useful when the the wrapper object is referenced as Object.
 * 
 * To be used only as a service for obtaining value objects. This service is
 * instantiated to a particular serializable object. Not to be used as a value
 * object (and passing it to methods) in itself: value objects should be
 * obtained from its {@link #getJson()}, {@link #getYaml()} and
 * {@link #getObject()} methods.
 *
 * @author A. Haanstra
 *
 * @param <S>
 */
public class WrappedSerializable<S extends Serializable> {

	public enum SerializationType {
		JSON, YAML;
	}

	private final S serializable;
	private final ObjectMapper objectToJsonMapper;
	private final ObjectMapper objectToYamlMapper;
	private final SerializationType defaultSerializationType;
	private final Logger log;

	public static <S extends Serializable> WrappedSerializableBuilder<S> builder(S serializable) {
		return new WrappedSerializableBuilder<>(serializable);
	}

	public static class WrappedSerializableBuilder<S extends Serializable> {

		private S serializable;
		private SerializationType defaultSerializationType;

		public WrappedSerializableBuilder(S serializable) {
			this.serializable = serializable;
		}

		public WrappedSerializableBuilder<S> defaultSerializationType(SerializationType defaultSerializationType) {
			this.defaultSerializationType = defaultSerializationType;
			return this;
		}

		/**
		 * If {@link #defaultSerializationType} was not set, the builder uses a
		 * {@link SerializationType#JSON} as {@link #defaultSerializationType}.
		 * 
		 * @return A {@link WrappedSerializable} instance.
		 */
		public WrappedSerializable<S> build() {
			if (defaultSerializationType == null) {
				defaultSerializationType = SerializationType.JSON;
			}
			ObjectMapper objectToJsonMapper = new ObjectMapper();
			ObjectMapper objectToYamlMapper = new ObjectMapper(new YAMLFactory());
			Logger log = LoggerFactory.getLogger(WrappedSerializable.class);
			return new WrappedSerializable<S>(serializable, defaultSerializationType, objectToJsonMapper,
					objectToYamlMapper, log);
		}

	}

	/**
	 * Constructs a WrappedSerializable object around a Serializable object and sets
	 * the default serialization type used by {@link #toString()}.
	 *
	 * @param serializable             The wrapped object used in the unit test
	 * @param defaultSerializationType The default serialization type used by
	 *                                 {@link #toString()}
	 */
	WrappedSerializable(S serializable, SerializationType defaultSerializationType, ObjectMapper objectToJsonMapper,
			ObjectMapper objectToYamlMapper, Logger log) {
		super();
		this.serializable = serializable;
		this.defaultSerializationType = defaultSerializationType;
		this.objectToJsonMapper = objectToJsonMapper;
		this.objectToYamlMapper = objectToYamlMapper;
		this.log = log;
	}

	/**
	 *
	 * @return The wrapped object
	 */
	public S getObject() {
		return serializable;
	}

	/**
	 *
	 * @return The JSON representation of the wrapped object
	 * @throws JsonProcessingException
	 */
	public String getJson() throws JsonProcessingException {
		return objectToJsonMapper.writeValueAsString(serializable);
	}

	/**
	 *
	 * @return The YAML representation of the wrapped object
	 * @throws JsonProcessingException
	 */
	public String getYaml() throws JsonProcessingException {
		return objectToYamlMapper.writeValueAsString(serializable);
	}

	/**
	 * Serializes the wrapped serializable object to the default serialization type.
	 *
	 */
	@Override
	public String toString() {
		try {
			switch (defaultSerializationType) {
			case JSON:
				return getJson();
			case YAML:
				return getYaml();
			default:
				throw new IllegalStateException("Unknown default serialization type set.");
			}
		} catch (JsonProcessingException e) {
			log.error("Problem encountered during serialization. Serializable.toString(): {}.", serializable.toString(),
					e);
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Returns the hashCode of this object, only dependent on the wrapped
	 * {@link Serializable}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(serializable);
	}

	/**
	 * Determines whether the wrapped {@link Serializable} objects are the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrappedSerializable<?> other = (WrappedSerializable<?>) obj;
		return Objects.equals(this.serializable, other.serializable);
	}

}
