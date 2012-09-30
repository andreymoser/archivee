/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package biz.bidi.archivee.commons.components;

import biz.bidi.archivee.commons.dao.IArchiveeGenericDAO;
import biz.bidi.archivee.commons.model.mongodb.App;
import biz.bidi.archivee.commons.model.mongodb.Context;
import biz.bidi.archivee.commons.model.mongodb.ContextIndex;
import biz.bidi.archivee.commons.model.mongodb.ContextQueue;
import biz.bidi.archivee.commons.model.mongodb.Dictionary;
import biz.bidi.archivee.commons.model.mongodb.DictionaryQueue;
import biz.bidi.archivee.commons.model.mongodb.LogQueue;
import biz.bidi.archivee.commons.model.mongodb.MasterIndex;
import biz.bidi.archivee.commons.model.mongodb.Pattern;
import biz.bidi.archivee.commons.model.mongodb.Template;

import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

/**
 * @author Andrey Bidinotto
 * @email andreymoser@bidi.biz
 * @since Sep 29, 2012
 */
public abstract class ArchiveeManagedComponent {

	/**
	 * DAOs instances - component will instantiate only the ones it uses
	 */
	protected IArchiveeGenericDAO<Pattern, Query<Pattern>, Key<Pattern>> patternDAO;
	protected IArchiveeGenericDAO<LogQueue, Query<LogQueue>, Key<LogQueue>> logQueueDAO;
	protected IArchiveeGenericDAO<App, Query<App>, Key<App>> appDAO;
	protected IArchiveeGenericDAO<Context, Query<Context>, Key<Context>> contextDAO;
	protected IArchiveeGenericDAO<ContextQueue, Query<ContextQueue>, Key<ContextQueue>> contextQueueDAO;
	protected IArchiveeGenericDAO<ContextIndex, Query<ContextIndex>, Key<ContextIndex>> contextIndexDAO;
	protected IArchiveeGenericDAO<Dictionary, Query<Dictionary>, Key<Dictionary>> dictionaryDAO;
	protected IArchiveeGenericDAO<DictionaryQueue, Query<DictionaryQueue>, Key<DictionaryQueue>> dictionaryQueueDAO;
	protected IArchiveeGenericDAO<Template, Query<Template>, Key<Template>> templateDAO;
	protected IArchiveeGenericDAO<MasterIndex, Query<MasterIndex>, Key<MasterIndex>> masterIndexDAO;

}
