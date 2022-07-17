package backtracking;

public interface TemplateMethod<P, C>
{
	P firstChoisePoint();

	P nextChoisePoint(P ps);

	P lastChoisePoint();

	C firstChoise(P ps);

	C nextChoise(C s);

	C lastChoise(P ps);

	boolean assignable(C choise, P choisePoint);

	void assign(C scelta, P choisePoint);

	void deassign(C scelta, P choisePoint);

	P previousChoisePoint(P choisePoint);

	C lastChoiseAssignedTo(P choisePoint);

	void writeSolution(int sol);

	default void solve()
	{
		solve(Integer.MAX_VALUE);
	}

	default void solve(int num_max_soluzioni)
	{ // template method
		int sol = 0;
		P ps = firstChoisePoint();
		C c = firstChoise(ps);
		boolean backtrack = false, 
				done = false;
		do
		{
			// forward
			while (!backtrack && sol < num_max_soluzioni)
			{
				if (assignable(c, ps))
				{
					assign(c, ps);
					if (ps.equals(lastChoisePoint()))
					{
						++sol;
						writeSolution(sol);
						deassign(c, ps);
						if (!c.equals(lastChoise(ps)))
							c = nextChoise(c);
						else
							backtrack = true;
					}
					else
					{
						ps = nextChoisePoint(ps);
						c = firstChoise(ps);
					}
				}
				else
					if (!c.equals(lastChoise(ps)))
						c = nextChoise(c);
					else
						backtrack = true;
			}
			done = ps.equals(firstChoisePoint())
					|| sol == num_max_soluzioni;
			while (backtrack && !done)
			{
				ps = previousChoisePoint(ps);
				c = lastChoiseAssignedTo(ps);
				deassign(c, ps);
				if (!c.equals(lastChoise(ps)))
				{
					c = nextChoise(c);
					backtrack = false;
				}
				else
					if (ps.equals(firstChoisePoint()))
						done = true;
			}
		}
		while (!done);
	}

}
