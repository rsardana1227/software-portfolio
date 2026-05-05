from Expense import *


class ExpensesLoader(object):
    """A class for loading expenses from a file.
    """

    # We do not have an __init__ function and will call the default constructor

    def import_expenses(self, expenses, file):
        """Reads data from the given file and stores the expenses in the given expenses dictionary, where the expense
        type is the key and the value is an Expense object with the parameters expense type and total amount for that
        expense type.

        The same expense type may appear multiple times in the given file, so add all the amounts for the same
        expense types.

        Ignore expenses with missing amounts. If a line contains both an expense type and an expense amount,
        they will be separated by a colon (:).

        You can assume that if they exist, expense types are one-word strings and the amounts are numerical and can
        be casted to a float data type.

        Strip any whitespace before or after the expense types and amounts. Blank lines should be ignored.

        Expenses are case-sensitive. "coffee" is different from "Coffee".

        This method will be called twice in the main function in expenses.py with the same dictionary, but different
        files.

        This method doesn’t return anything.  Rather, it updates the given expenses dictionary based
        on the expenses in the given file.

        For example, after loading the expenses from the file, the expenses dictionary should look like
        this: {'food': Expense('food', 5.00), 'coffee': Expense('coffee', 12.40),
               'rent': Expense('rent', 825.00), 'clothes': Expense('clothes', 45.00),
               'entertainment': Expense('entertainment', 135.62), 'music': Expense('music', 324.00),
               'family': Expense('family', 32.45)}

        Note: You are not expected to handle negative numbers in your code
        """

        # TODO insert your code
        # open file in read mode and go through each line of the file
        with open(file, 'r') as f:
            for line in f:
                # remove any whitespace before/after and newline characters
                line = line.strip()
                # skip blank lines
                if line == '':
                    continue
                # skip any lines that are not separated by a colon which separates expenses
                if ':' not in line:
                    continue
                # now that the line has satisfied these basic conditions, we can split into expense type and amount
                line = line.split(':')

                # define expense type and amount and remove whitespace
                expense_type = line[0].strip()
                amount = line[1].strip()
                # skip any lines with a missing amount
                if amount == '':
                    continue
                amount = float(amount)
                # if expense already exists, add to current total. otherwise, create a new Expense object
                if expense_type in expenses:
                    expenses[expense_type].add_amount(amount)
                else:
                    expenses[expense_type] = Expense(expense_type, amount)


