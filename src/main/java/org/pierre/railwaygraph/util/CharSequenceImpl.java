/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.util;

/**
 *
 * @author Pierre
 */
public class CharSequenceImpl implements CharSequence {
    private String s;

    public CharSequenceImpl(String s) {
        this.s = s;
    }
    

    @Override
    public int length() {
        return s.length();
    }

    @Override
    public char charAt(int index) {
        if ((index < 0) || (index >= s.length())) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return s.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (end > s.length()) {
            throw new StringIndexOutOfBoundsException(end);
        }
        if (start > end) {
            throw new StringIndexOutOfBoundsException(start - end);
        }
        return s.subSequence(start, end);
    }
    
}
