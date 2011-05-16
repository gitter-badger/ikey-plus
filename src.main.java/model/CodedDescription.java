package model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Florian Causse
 * @created 06-avr.-2011
 */
public class CodedDescription {

	private String id = null;
	private Map<ICharacter, Object> description = null;

	public CodedDescription() {
		this.description = new HashMap<ICharacter, Object>();
	}

	/**
	 * get the description (all Characters)
	 * 
	 * @return Map<ICharacter, Object>
	 */
	public Map<ICharacter, Object> getDescription() {
		return description;
	}

	/**
	 * set the description (all Characters)
	 * 
	 * @param Map
	 *            <ICharacter, Object>
	 */
	public void setDescription(Map<ICharacter, Object> description) {
		this.description = description;
	}

	/**
	 * get the description for one character
	 * 
	 * @param ICharacter
	 *            , the key
	 * @return Object, the character description
	 */
	public Object getCharacterDescription(ICharacter character) {
		return description.get(character);
	}

	/**
	 * add the description for one character
	 * 
	 * @param ICharacter
	 *            , the key
	 * @param Object
	 *            , the description concerning the character
	 */
	public void addCharacterDescription(ICharacter character,
			Object characterDescription) {
		description.put(character, characterDescription);
	}

	/**
	 * remove the description for one character
	 * 
	 * @param ICharacter
	 *            , the key
	 */
	public void removeCharacterDescription(ICharacter character) {
		description.remove(character);
	}

	/**
	 * get the identifier
	 * 
	 * @return String, CodedDescription identifier
	 */
	public String getId() {
		return id;
	}

	/**
	 * set the identifier
	 * 
	 * @param String
	 *            , CodedDescription identifier
	 */
	public void setId(String id) {
		this.id = id;
	}
}