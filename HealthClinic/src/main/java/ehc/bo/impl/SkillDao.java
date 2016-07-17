package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class SkillDao {
	private static SkillDao instance = new SkillDao();
	
	private SkillDao() {
	}

	public static SkillDao getInstance() {
		return instance;
	}
	
	public Skill findById(long id) {
		Session session = HibernateUtil.getCurrentSession();	
		Skill skill = session.get(Skill.class, id);	
		return skill;
	} 
	
	public Skill findByName(String name) {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Skill s WHERE s.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Skill skill = (Skill)results.get(0);		
		return skill;
	}
	
	public List<Skill> getAll() {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Skill";
		Query query = session.createQuery(hql);
		List results = query.list();	
		return results;
	}

}
