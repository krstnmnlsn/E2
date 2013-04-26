package com.coursemingle.shared;

public class Instructor {

	private String instructor_name;
	private int instructor_id;

	public String getInstructor_name() {
		return instructor_name;
	}
	public int getInstructor_id() {
		return instructor_id;
	}
	public void setInstructor_name(String instructor_name) {
		this.instructor_name = instructor_name;
	}
	public void setInstructor_id(int instructor_id) {
		this.instructor_id = instructor_id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + instructor_id;
		result = prime * result
				+ ((instructor_name == null) ? 0 : instructor_name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instructor other = (Instructor) obj;
		if (instructor_id != other.instructor_id)
			return false;
		if (instructor_name == null) {
			if (other.instructor_name != null)
				return false;
		} else if (!instructor_name.equals(other.instructor_name))
			return false;
		return true;
	}
	
}
