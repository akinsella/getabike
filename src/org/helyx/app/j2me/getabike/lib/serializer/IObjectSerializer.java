/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.serializer;

import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;

public interface IObjectSerializer {

	byte[] serialize(Object object) throws SerializerException;

	Object deserialize() throws SerializerException;
	
	void dispose();

	boolean matches(RecordFilter recordFilter) throws SerializerException;
	
	void ensureInputByteBufferLength(int length);

	byte[] getByteArray();
	
	byte[] getByteArray(int length);

}
