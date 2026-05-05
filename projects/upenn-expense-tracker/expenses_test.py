import unittest

from ExpensesLoader import *
from ExpensesManager import *


class Expenses_Test(unittest.TestCase):

    def setUp(self):
        """The setUp function runs before every test function."""

        # create expenses dictionary and populate with data
        self.expenses = {'food': Expense('food', 5.00), 'coffee': Expense('coffee', 12.40),
                         'rent': Expense('rent', 825.00), 'clothes': Expense('clothes', 45.00),
                         'entertainment': Expense('entertainment', 135.62), 'music': Expense('music', 324.00),
                         'family': Expense('family', 32.45)}

    def test_import_expenses(self):
        # import expenses files
        expenses = {}

        # create instance of ExpensesLoader class
        expenses_loader = ExpensesLoader()

        # call import_expenses method in ExpensesLoader class
        # to import expense files and store in dictionary
        expenses_loader.import_expenses(expenses, 'expenses.txt')
        expenses_loader.import_expenses(expenses, 'expenses_2.txt')

        # test existing expense amounts
        self.assertAlmostEqual(45, expenses['clothes'].amount)
        self.assertAlmostEqual(12.40, expenses['coffee'].amount)
        self.assertAlmostEqual(135.62, expenses['entertainment'].amount)

    def test_get_expense(self):
        # create instance of ExpensesManager class
        expenses_manager = ExpensesManager()

        # test getting expenses based on expense type
        self.assertAlmostEqual(12.40, expenses_manager.get_expense(self.expenses, "coffee").amount)

        # test non-existing expense types
        self.assertEqual(None, expenses_manager.get_expense(self.expenses, "phone"))

        # TODO insert 2 additional test cases
        #  Hint(s): Test both existing and non-existing expense types
        # testing existing expense like family to see if it gets the same value shown in setup
        self.assertAlmostEqual(32.45, expenses_manager.get_expense(self.expenses, "family").amount)
        # testing non-existing expense such as rent but capitalized version of rent
        self.assertEqual(None, expenses_manager.get_expense(self.expenses, "RENT"))


    def test_add_expense(self):
        # create instance of ExpensesManager class
        expenses_manager = ExpensesManager()

        # test adding a new expense
        expenses_manager.add_expense(self.expenses, "fios", 84.5)
        self.assertAlmostEqual(84.5, self.expenses.get("fios").amount)

        # TODO insert 2 additional test cases
        #  Hint(s): Test adding to existing expenses
        # test adding to existing expense "food"
        expenses_manager.add_expense(self.expenses, "food", 5)
        self.assertAlmostEqual(10.00, self.expenses.get("food").amount)

        # test adding to another existing expense (coffee) with a decimal
        expenses_manager.add_expense(self.expenses, "coffee", 2.5)
        self.assertAlmostEqual(14.90, self.expenses.get("coffee").amount)

    def test_deduct_expense(self):
        # create instance of ExpensesManager class
        expenses_manager = ExpensesManager()

        # test deducting from expense
        expenses_manager.deduct_expense(self.expenses, "coffee", .99)
        self.assertAlmostEqual(11.41, self.expenses.get("coffee").amount)

        # test deducting from expense
        expenses_manager.deduct_expense(self.expenses, "entertainment", 100)
        self.assertAlmostEqual(35.62, self.expenses.get("entertainment").amount)

        # TODO insert 2 additional test cases
        #  Hint(s):
        #   Test deducting too much from expense
        #   Test deducting from non-existing expense
        # test deducting more than existing amount - should raise RuntimeError
        with self.assertRaises(RuntimeError):
            expenses_manager.deduct_expense(self.expenses, "food", 100)

        # test deducting from a non-existing expense - should not crash or add new key
        expenses_manager.deduct_expense(self.expenses, "phone", 10)
        self.assertNotIn("phone", self.expenses)

    def test_update_expense(self):
        # create instance of ExpensesManager class
        expenses_manager = ExpensesManager()

        # test updating an expense
        expenses_manager.update_expense(self.expenses, "clothes", 19.99)
        self.assertAlmostEqual(19.99, expenses_manager.get_expense(self.expenses, "clothes").amount)

        # TODO insert 2 additional test cases
        #  Hint(s):
        #   Test updating an expense
        #   Test updating a non-existing expense
        # test updating existing expense (coffee)
        expenses_manager.update_expense(self.expenses, "coffee", 18.75)
        self.assertAlmostEqual(18.75, expenses_manager.get_expense(self.expenses, "coffee").amount)

        # test updating a non-existing expense which should not add a new key
        expenses_manager.update_expense(self.expenses, "phone", 42.50)
        self.assertNotIn("phone", self.expenses)

    def test_sort_expenses(self):
        # create instance of ExpensesManager class
        expenses_manager = ExpensesManager()

        # test sorting expenses by 'expense_type'
        expense_type_sorted_expenses = [('clothes', 45.0),
                                        ('coffee', 12.4),
                                        ('entertainment', 135.62),
                                        ('family', 32.45),
                                        ('food', 5.0),
                                        ('music', 324.0),
                                        ('rent', 825.0)]

        self.assertListEqual(expense_type_sorted_expenses,
                             expenses_manager.sort_expenses(self.expenses, "expense_type"))

        # TODO insert 1 additional test case
        #   Hint: Test sorting expenses by 'amount'
        # test sorting expenses by amount instead of expense type
        amount_sorted_expenses = [('rent', 825.0),
                                  ('music', 324.0),
                                  ('entertainment', 135.62),
                                  ('clothes', 45.0),
                                  ('family', 32.45),
                                  ('coffee', 12.4),
                                  ('food', 5.0)]

        self.assertListEqual(amount_sorted_expenses,
                             expenses_manager.sort_expenses(self.expenses, "amount"))

    def test_export_expenses(self):
        # create instance of ExpensesManager class
        expenses_manager = ExpensesManager()

        # test export with existing expense types
        file = 'export1.txt'
        expense_types = ['coffee', 'clothes']
        expenses_manager.export_expenses(self.expenses, expense_types, file)

        # read the exported file
        f = open(file)
        lines = f.readlines()
        f.close()
        self.assertEqual('coffee: 12.40', lines[0].strip())
        self.assertEqual('clothes: 45.00', lines[1].strip())

        # TODO insert 1 additional test case
        #   Hint: Test exporting with some non-existent expense types.
        # test export with non-existent expense types like nutella here
        file2 = 'export2.txt'
        expense_types2 = ['coffee', 'nutella', 'rent']
        expenses_manager.export_expenses(self.expenses, expense_types2, file2)

        # read the exported file
        f2 = open(file2)
        lines2 = f2.readlines()
        f2.close()

        # only existing expense types should be written
        self.assertEqual('coffee: 12.40', lines2[0].strip())
        self.assertEqual('rent: 825.00', lines2[1].strip())
        self.assertEqual(2, len(lines2))


if __name__ == '__main__':
    unittest.main()
