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
            team.setName("팀A");
            em.persist(team);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(10);
            member.setTeam(team);
//            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.setTeam(team);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(10);
            member3.setTeam(teamB);

            member.setTeam(team);

            em.persist(member);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

/*            List<Member> query = em.createQuery("select m from Member m", Member.class).getResultList();
            TypedQuery<String> query1 = em.createQuery("select m.username from Member m", String.class); //반환타입 명확할 때
            String result = query1.getSingleResult();//결과가 정확하게 하나가 나와야함. (빈값이거나 하나 이상의 값이 나올 경우 Exception터짐)

            System.out.println("result = " + result);

            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + result1);
            */


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


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
/*
            String query = "select group_concat(m.username) from Member m"; //방법2

            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }
*/

            // 묵시적 조인: inner 조인 발생 (성능 튜닝 힘들다..) t.해서 뭐만 하면 다 이너조인 됨.. 사용하지말자ㅠㅠ성능튜닝 망해요
//            String query2 = "select t.members from Team t";
            //명시적 조인 (join 키워드 직접사용) (별칭으로 조인 가져와서 사용하는것)
/*            String query2 = "select m.username from Team t join t.members m";
            List result = em.createQuery(query2).getResultList();
            for (Object o : result) {
                System.out.println("o = " + o);
            }*/

///////////////////////////////////fetch join ↓///////////////////////////////////////////////////////////////////////////////////


            //다대일 조회시
            //쿼리가 겁나게 많이 나간다 아래 방법으로 하며는!!! (회원 100명 조회하게대면 대략 미친듯이 나가겠쥬? 최악최악)
/*            String query = "select m From Member m";

            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member1 : result) {
                System.out.println("member1 = " + member1.getUsername() + ", " + member1.getTeam().getName());
            }*/



            /**
             * 페칭 조인에는 별칭을 줄 수 없다 [ join fetch t.members m ] 이런식으로 안됨
             * 페치 조인은 최적화가 필요한 곳으로 적용한다.
             * 즉, 모든 것을 페치 조인으로 해결할 수 없다.
             */
            //다대일 조회시
            //그래서 이렇게 한방쿼리를 한다. (그러면 한방에 쿼리 나간거에서 데이터 가져온다!!! 실행해서 확인해보자!)**실무에서 많이 쓰임!!**
/*            String query = "select m From Member m join fetch m.team";

            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member1 : result) {
                System.out.println("member1 = " + member1.getUsername() + ", " + member1.getTeam().getName());
            }*/


            //반대로 Team으로 조회시(일대다) : 데이터 갯수가 뻥튀기 댑니다!(나중에 보면 이해가 잘 안될 것 같다. 김영한선생님 수업 다시 들어보길!)
//            String query = "select t From Team t join fetch t.members"; //중복값이 나온다.
//            String query = "select distinct t From Team t join fetch t.members"; //중복값 제거 (같은 식별자 엔티티 제거)

            /*List<Team> result = em.createQuery(query, Team.class).getResultList();

            for (Team team1 : result) {
                System.out.println("team1 = " + team1.getName() + "||" + team1.getMembers().size());
                for (Member member1 : team1.getMembers()) {
                    System.out.println("member = " + member1);
                }
            }*/

            //일대다(컬렉션) 페이징 처리시(아래 코드로 하면 나중에 팀 맴버가 늘어날수록 쿼리가 오지게 나간다. 성능 딸리겠죠?)
            // Team.class에     @BatchSize(size = 100) 추가했다.
            //또는 <property name="hibernate.default_batch_fetch_size" value="100"/> 추가
    /*        String query = "select t From Team t"; //과감하게 fetch 지워버림

            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result.size() = " + result.size());

            for (Team team1 : result) {
                System.out.println("team1 = " + team1.getName() + "||" + team1.getMembers().size());
                for (Member member1 : team1.getMembers()) {
                    System.out.println("member = " + member1);
                }
            }*/


///////////////////////////////////중급문법 엔티티 직접사용 ↓///////////////////////////////////////////////////////////////////////////////////

       /*     String query = "select m from Member m where m = :member";

            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("member", member)
                    .getSingleResult();
            System.out.println("findMember = " + findMember);*/

///////////////////////////////////중급문법 엔티티 (외래 키 값) 직접사용 ↓///////////////////////////////////////////////////////////////////////////////////

/*            String query = "select m from Member m where m.team = :team";

            List<Member> members = em.createQuery(query, Member.class)
                    .setParameter("team", team)
                    .getResultList();
            for (Member member1 : members) {
                System.out.println("member1 = " + member1);
            }*/


///////////////////////////////////Named 쿼리리 ↓//////////////////////////////////////////////////////////////////////////////////

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
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
