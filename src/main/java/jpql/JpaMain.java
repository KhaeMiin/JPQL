package jpql;

import javax.persistence.*;
import java.awt.print.Book;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

/*            List<Member> query = em.createQuery("select m from Member m", Member.class).getResultList();
            TypedQuery<String> query1 = em.createQuery("select m.username from Member m", String.class); //반환타입 명확할 때
            String result = query1.getSingleResult();//결과가 정확하게 하나가 나와야함. (빈값이거나 하나 이상의 값이 나올 경우 Exception터짐)

            System.out.println("result = " + result);

            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + result1);
            */

            em.flush();
            em.clear();

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            // 1. 조인시 올바른? (보기 편한) 쿼리
//            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

            // 2. 임베디드 타입 가져오기
//            em.createQuery("select o.address from Order o", Address.class).getResultList();

   /*         List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());*/


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            //페이징
            /*for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member1" + i);
                member.setAge(i);
                em.persist(member);
            }*/
/*            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10) //데이터 몇개를 가져올건지
                    .getResultList();
            System.out.println("result = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }*/

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //Join
            //1. 이너조인(쿼리문에 inner 생략 가능), 2.아웃조인 left outer (outer역시 생략 가능)
//            String query = "select m from Member m inner join m.team t";
            //3. 세타 조인 (막 조인) : Team name과 Member username 이 동일한 것 찾아서 출력
//            String query = "select m from Member m, Team t where m.username = t.name";

            //on절 (join에서는 where
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
            //on절 세타 조인인 경우
//            String query = "select m from Member m left join Team t on m.username = t.name";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

            //MemberType에 ADMIN 인 경우만 출력
/*
            String query = "select m.username, 'HELLO', TRUE from Member m " +
                            "where m.type = :userType";
            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }
*/




            //1. 조건식
/*
            String query =
                    "select " +
                            "case when m.age <= 10 then '학생요금'" +
                            "     when m.age >= 60 then '경로요금'" +
                            "     else '일반요금'" +
                            "end " +
                            "from Member m";
*/

            //사용자 이름이 없으면 '이름 없는 회원' 반환
            /*member.setUsername(null);
            em.flush();
            em.clear();
//            String query = "select coalesce(m.username, '이름 없는 회원') from Member m ";

            // 이름이 관리자일 경우 null 반환해라!!
            String query = "select nullif(m.username, '관리자') from Member m ";
            */

            //함수들
//            String query = "select concat('a','b') From Member m "; //주로 컴켓 쓰신대!!!(문자 두개 합칠때)
//            String query = "select substring(m.username, 2, 3) from Member m"; //문자 자를때
//            String query = "select locate('de', 'asdefgh') from Member m";

            //사용자 정의 함수 호출(MyH2Dialect 클래스 참조)
//            String query = "select function('group_concat', m.username) from Member m"; //방법1
            String query = "select group_concat(m.username) from Member m"; //방법2

            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            tx.commit();//쓰기지연 SQL 저장소에 SQL문을 DB에 보냄
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
