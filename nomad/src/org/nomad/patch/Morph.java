package org.nomad.patch;

class Morph {

	private int sectionIndex, moduleIndex, paramIndex, morphIndex, morphRange;

	Morph (int newSectionIndex, int newModuleIndex, int newParamIndex, int newMorphIndex, int newMorphRange) {
		sectionIndex = newSectionIndex;
		moduleIndex = newModuleIndex;
		paramIndex = newParamIndex;
		morphIndex = newMorphIndex;
		morphRange = newMorphRange;
	}
	
	public int getSectionIndex() {
		return sectionIndex;
	}
	
	public int getModuleIndex() {
		return moduleIndex;
	}
	
	public int getParamIndex() {
		return paramIndex;
	}
	
	public int getMorphIndex() {
		return morphIndex;
	}
	
	public int getMorphRange() {
		return morphRange;
	}
}	
