/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.blackhole.Model;


public class BlackHoleTile {
    public int player;
    public int value;

    BlackHoleTile(int player, int value) {
        this.player = player;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackHoleTile)) return false;

        BlackHoleTile that = (BlackHoleTile) o;

        if (player != that.player) return false;
        return value == that.value;

    }

    @Override
    public int hashCode() {
        int result = player;
        result = 31 * result + value;
        return result;
    }
}